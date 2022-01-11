package com.example.githubusersearch.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRoom(roomEntity: List<RoomEntity>)

//    @Query("SELECT * FROM tb_rooms WHERE login LIKE :query || '%'")
    @Query("SELECT * FROM tb_rooms")
    fun getRooms(): PagingSource<Int, RoomEntity>

    @Query("UPDATE tb_rooms SET isFavorite = :favorite WHERE node_id = :id")
    fun updateFavorite(id: String, favorite: Boolean)

    @Query("DELETE FROM tb_rooms")
    suspend fun clearRooms()

}