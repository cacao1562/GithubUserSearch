package com.example.githubusersearch.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.githubusersearch.api.GithubService
import com.example.githubusersearch.database.RemoteKeys
import com.example.githubusersearch.database.RoomEntity
import com.example.githubusersearch.database.RoomInfoDatabase
import com.example.githubusersearch.model.asEntity
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class UserListMediator @Inject constructor(
    private val githubService: GithubService,
    private val database: RoomInfoDatabase,
    private val query: String
): RemoteMediator<Int, RoomEntity>() {

    private val remoteKeyDao = database.remoteKeyDao()
    private val roomDao = database.roomDao()
    private val favoriteDao = database.favoriteDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RoomEntity>
    ): MediatorResult {
        try {
            val loadKey: Int = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                    Log.d("yhw", "[UserListMediator>load] REFRESH prev=${remoteKey?.prevKey}\n\tnext=${remoteKey?.nextKey} [38 lines]")
                    remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE
                }

                LoadType.PREPEND -> {
                    val remoteKey = getRemoteKeyForFirstItem(state)
                    Log.d("yhw", "[UserListMediator>load] PREPEND> prev=${remoteKey?.prevKey}, next=${remoteKey?.nextKey} [44 lines]")
//                    Log.d("yhw", "[UserListMediator>load] PREPEND prev=${remoteKey?.prevKey}\n\tnext=${remoteKey?.nextKey} [38 lines]")
                    val prevKey = remoteKey?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    prevKey
//                    return MediatorResult.Success(true)
//                    val key = database.withTransaction {
//                        database.remoteKeyDao().allkeys().lastOrNull() // Workaround
//                    }
////                    Log.d("yhw", "[UserListMediator>load] PREPEND>>> prev=${remoteKey?.prevKey}, next=${remoteKey?.nextKey} [44 lines]")
//                    key?.prevKey ?: return MediatorResult.Success(true)
                }

                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)
//                    Log.d("yhw", "[UserListMediator>load] APPEND prev=${remoteKey?.prevKey}\n\tnext=${remoteKey?.nextKey} [38 lines]")
                    val nextKey = remoteKey?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                    nextKey

                }
            }

            val apiResponse = githubService.queryGithubUsers(query, loadKey, state.config.pageSize)
            val rooms = apiResponse.asEntity()
            val endOfPaginationReached = apiResponse.items.isNullOrEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys()
                    roomDao.clearRooms()
                }

                val prevKey = if (loadKey == STARTING_PAGE) null else loadKey - 1
                val nextKey = if (endOfPaginationReached) null else loadKey + 1
                val keys = rooms.map { roomInfo ->
                    roomInfo.isFavorite = favoriteDao.isFavorite(roomInfo.node_id)
                    RemoteKeys(roomInfo.node_id, nextKey = nextKey, prevKey = prevKey)
                }

                remoteKeyDao.insertAll(keys)
                roomDao.insertAllRoom(rooms)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, RoomEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.node_id?.let { id ->
                remoteKeyDao.remoteKeysRoomId(id)
            }
        }
    }

//    92413636
//    79597906
//    ABCxFF
//    1378061213
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, RoomEntity>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { last ->
            remoteKeyDao.remoteKeysRoomId(last.node_id)

        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, RoomEntity>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { first ->
            Log.d("yhw", "[UserListMediator>getRemoteKeyForLastItem] lastId=${first.id} [109 lines]")
            Log.d("yhw", "[UserListMediator>getRemoteKeyForLastItem] lastLogin=${first.login} [109 lines]")
            Log.d("yhw", "[UserListMediator>getRemoteKeyForLastItem] lastHash=${first.login.hashCode().toLong()} [109 lines]")
            remoteKeyDao.remoteKeysRoomId(first.node_id)
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
    }

}