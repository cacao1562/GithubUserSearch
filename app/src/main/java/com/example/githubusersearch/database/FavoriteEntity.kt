package com.example.githubusersearch.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "tb_favorite")
data class FavoriteEntity(
    @NonNull @PrimaryKey @ColumnInfo(name = "node_id") val node_id: String,
    @ColumnInfo(name = "id") val id: Double,
    @ColumnInfo(name = "login") val login: String? = "",
    @ColumnInfo(name = "avatar_url") val avatar_url: String? = "",
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean? = false,
    @ColumnInfo(name = "date") @TypeConverters(Converters::class) var date: Date? = Calendar.getInstance(Locale.KOREA).time
)
