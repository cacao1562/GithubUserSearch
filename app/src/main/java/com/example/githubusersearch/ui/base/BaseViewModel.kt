package com.example.githubusersearch.ui.base

import androidx.lifecycle.ViewModel
import androidx.room.withTransaction
import com.example.githubusersearch.database.FavoriteEntity
import com.example.githubusersearch.database.RoomInfoDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val database: RoomInfoDatabase
) : ViewModel() {

    fun udpateFavorite(nodeId: String, id: Double, login: String, avatar_url: String, selected: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            database.withTransaction {
                database.roomDao().updateFavorite(nodeId, !selected)
                if (selected) {
                    database.favoriteDao().deleteFavoriteById(nodeId)
                }else {
                    database.favoriteDao().insertFavorite(FavoriteEntity(nodeId, id, login, avatar_url,true))
                }
            }
        }
    }

}