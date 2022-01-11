package com.example.githubusersearch.api

import com.example.githubusersearch.model.ResponseSearch
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubService {

    @Headers("Authorization: token [token]")
    @GET("search/users")
    suspend fun queryGithubUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("page_per") page_per: Int,
    ): ResponseSearch
}