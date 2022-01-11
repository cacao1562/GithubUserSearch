package com.example.githubusersearch.ui.userlist

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.githubusersearch.database.RoomInfoDatabase
import com.example.githubusersearch.repository.RoomsRepository
import com.example.githubusersearch.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val database: RoomInfoDatabase,
    private val roomsRepository: RoomsRepository
): BaseViewModel(database) {

    private val remoteSearchWord: MutableStateFlow<String> = MutableStateFlow("")
    val datalist = remoteSearchWord
        .filter { it.isNullOrEmpty().not() }
        .distinctUntilChanged()
        .flatMapLatest {
            roomsRepository.getRooms(it).cachedIn(viewModelScope)
        }

    fun setRemoteSearchWord(word: String) {
        remoteSearchWord.value = word
    }

}