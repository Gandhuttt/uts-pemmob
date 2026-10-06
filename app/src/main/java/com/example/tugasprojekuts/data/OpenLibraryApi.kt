package com.example.tugasprojekuts.data

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") keyword: String,
        @Query("limit") limit: Int = 20
    ): SearchResponse
}
