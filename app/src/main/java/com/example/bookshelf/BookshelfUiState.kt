package com.example.bookshelf

import com.example.bookshelf.api.BookItem

sealed class BookshelfUiState {
    object Initial : BookshelfUiState()
    object Loading : BookshelfUiState()
    data class Success(val books: List<BookItem>) : BookshelfUiState()
    object Empty : BookshelfUiState()
    data class Error(val message: String) : BookshelfUiState()
}