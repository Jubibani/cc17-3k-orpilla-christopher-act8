package com.example.bookshelf.api

import com.example.bookshelf.data.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApi {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): BookResponse
}