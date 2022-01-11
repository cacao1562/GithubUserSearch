package com.example.githubusersearch.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.example.githubusersearch.api.GithubService
import com.example.githubusersearch.database.RoomEntity
import com.example.githubusersearch.database.RoomInfoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomsRepository @Inject constructor(
    private val githubService: GithubService,
    private val database: RoomInfoDatabase
) {

    fun getRooms(query: String): Flow<PagingData<RoomEntity>> {
        CoroutineScope(Dispatchers.IO).launch {
            database.withTransaction {
                database.remoteKeyDao().clearRemoteKeys()
                database.roomDao().clearRooms()
            }
        }
        val pagingSourceFactory = { database.roomDao().getRooms() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, maxSize = 300, enablePlaceholders = true),
            remoteMediator = UserListMediator(githubService, database, query),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 80
    }
}