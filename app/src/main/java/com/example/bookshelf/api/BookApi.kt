package com.example.bookshelf.api

import com.example.bookshelf.data.Book
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApi {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): Book
}