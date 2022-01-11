package com.example.githubusersearch.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM tb_favorite WHERE login LIKE :query || '%' ORDER BY date DESC")
    fun getFavorites(query: String): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM tb_favorite ORDER BY date DESC")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT isFavorite FROM tb_favorite WHERE node_id = :id")
    fun isFavorite(id: String): Boolean

    @Query("DELETE FROM tb_favorite WHERE node_id = :id")
    fun deleteFavoriteById(id: String): Int

}