package com.example.bookshelf.data

import com.example.bookshelf.network.BookApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository {
    private val bookApiService = BookApi.retrofitService

    suspend fun searchBooks(query: String): List<Book> {
        return withContext(Dispatchers.IO) {
            val response = bookApiService.searchBooks(query)
            val books = response.items ?: emptyList()
            books.map { book ->
                val bookInfo = bookApiService.getBookInfo(book.id)
                bookInfo.copy(volumeInfo = bookInfo.volumeInfo.copy(
                    imageLinks = bookInfo.volumeInfo.imageLinks?.copy(
                        thumbnail = bookInfo.volumeInfo.imageLinks.thumbnail?.replace("http:", "https:")
                    )
                ))
            }
        }
    }
}