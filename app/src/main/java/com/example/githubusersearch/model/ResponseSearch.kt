package com.example.githubusersearch.model

import com.example.githubusersearch.database.RoomEntity

data class ResponseSearch(
    val incomplete_results: Boolean,
    val items: List<SearchItem>,
    val total_count: Int
)

data class SearchItem(
    val login: String,
    val id: Double,
    val avatar_url: String,
    val node_id: String
)

fun ResponseSearch.asEntity(): List<RoomEntity> {
    return if (items.isNullOrEmpty().not()) {
        items.map {
            RoomEntity(node_id = it.node_id, id = it.id, login = it.login, avatar_url = it.avatar_url)
        }
    }else {
        emptyList()
    }
}