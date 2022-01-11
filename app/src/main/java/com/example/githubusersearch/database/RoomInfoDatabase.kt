package com.example.githubusersearch.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*


@Database(entities = [RoomEntity::class, RemoteKeys::class, FavoriteEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RoomInfoDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun remoteKeyDao(): RemoteKeysDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}