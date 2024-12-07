package com.example.bookshelf.data

import com.example.bookshelf.api.BookApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository(private val bookApi: BookApi) {
    suspend fun searchBooks(query: String): Result<List<Book>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookApi.searchBooks(query)
                // Assuming the response is the BookResponse object itself
                Result.success(response.items ?: emptyList())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}