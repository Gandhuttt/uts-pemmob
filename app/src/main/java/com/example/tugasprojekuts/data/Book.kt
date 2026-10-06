package com.example.tugasprojekuts.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Book(
    val key: String?,
    val title: String?,
    @SerializedName("author_name") val authorName: List<String>?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?,
    @SerializedName("edition_count") val editionCount: Int?,
    val language: List<String>?
) : Serializable

data class SearchResponse(val docs: List<Book>?)
