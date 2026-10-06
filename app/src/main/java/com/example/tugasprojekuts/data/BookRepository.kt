package com.example.tugasprojekuts.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookRepository {
    private val api: OpenLibraryApi = Retrofit.Builder()
        .baseUrl("https://openlibrary.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenLibraryApi::class.java)

    suspend fun search(keyword: String): List<Book> = api.searchBooks(keyword).docs.orEmpty()
}
