package com.example.bookshelf.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.Book
import com.example.bookshelf.data.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookshelfViewModel(
    private val repository: BookRepository = BookRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookshelfUiState>(BookshelfUiState.Initial)
    val uiState = _uiState.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun searchBooks() {
        if (searchQuery.isBlank()) {
            _uiState.value = BookshelfUiState.Error("Search query cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = BookshelfUiState.Loading
            try {
                val books = repository.searchBooks(searchQuery)
                _uiState.value = if (books.isEmpty()) {
                    BookshelfUiState.Empty
                } else {
                    BookshelfUiState.Success(books)
                }
            } catch (e: Exception) {
                Log.e("BookshelfViewModel", "Error searching books", e)
                _uiState.value = BookshelfUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

sealed class BookshelfUiState {
    object Initial : BookshelfUiState()
    object Loading : BookshelfUiState()
    object Empty : BookshelfUiState()
    data class Success(val books: List<Book>) : BookshelfUiState()
    data class Error(val message: String) : BookshelfUiState()
}