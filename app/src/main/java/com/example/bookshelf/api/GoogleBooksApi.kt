package com.example.bookshelf.api

import com.example.bookshelf.data.ImageLinks
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): BooksResponse
}

data class BooksResponse(
    val items: List<BookItem>
)

data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?
)