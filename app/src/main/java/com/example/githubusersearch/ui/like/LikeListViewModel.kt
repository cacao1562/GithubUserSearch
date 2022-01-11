package com.example.githubusersearch.ui.like

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubusersearch.database.RoomInfoDatabase
import com.example.githubusersearch.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class LikeListViewModel @Inject constructor(
    private val database: RoomInfoDatabase
): BaseViewModel(database) {

    private val localSearchWord: MutableStateFlow<String> = MutableStateFlow("")
    val likeList = localSearchWord
        .debounce(400)
        .flatMapLatest {
            if (it.isEmpty()) database.favoriteDao().getFavorites()
            else database.favoriteDao().getFavorites(it)
        }.asLiveData(viewModelScope.coroutineContext)

    fun setLocalSearchWord(word: String) {
        localSearchWord.value = word
    }

}