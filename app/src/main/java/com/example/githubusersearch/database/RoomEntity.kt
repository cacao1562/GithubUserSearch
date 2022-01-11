package com.example.githubusersearch.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_rooms")
data class RoomEntity @JvmOverloads constructor(
    @NonNull @PrimaryKey @ColumnInfo(name = "node_id") val node_id: String,
    @ColumnInfo(name = "id") val id: Double,
    @ColumnInfo(name = "login") val login: String? = "",
    @ColumnInfo(name = "avatar_url") val avatar_url: String? = "",
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean? = false
)