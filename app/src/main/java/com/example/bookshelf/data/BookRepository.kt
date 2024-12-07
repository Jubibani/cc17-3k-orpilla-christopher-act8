package com.example.bookshelf.data

import com.example.bookshelf.network.BookApi

class BookRepository {
    private val bookApiService = BookApi.retrofitService

    suspend fun searchBooks(query: String): List<Book> {
        val response = bookApiService.searchBooks(query)
        return response.items?.map { book ->
            val bookInfo = bookApiService.getBookInfo(book.id)
            Book(
                id = bookInfo.id,
                volumeInfo = VolumeInfo(
                    title = bookInfo.volumeInfo.title,
                    authors = bookInfo.volumeInfo.authors,
                    publisher = bookInfo.volumeInfo.publisher,
                    publishedDate = bookInfo.volumeInfo.publishedDate,
                    description = bookInfo.volumeInfo.description,
                    pageCount = bookInfo.volumeInfo.pageCount,
                    categories = bookInfo.volumeInfo.categories,
                    imageLinks = ImageLinks(
                        smallThumbnail = bookInfo.volumeInfo.imageLinks?.smallThumbnail?.replace("http:", "https:"),
                        thumbnail = bookInfo.volumeInfo.imageLinks?.thumbnail?.replace("http:", "https:")
                    )
                )
            )
        } ?: emptyList()
    }
}