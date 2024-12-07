package com.example.bookshelf.network

import com.example.bookshelf.api.BookApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BookApiService {
    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val bookApi: BookApi = retrofit.create(BookApi::class.java)
}