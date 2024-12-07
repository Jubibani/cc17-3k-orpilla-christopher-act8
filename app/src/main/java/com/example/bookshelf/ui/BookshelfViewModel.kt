package com.example.bookshelf.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.Book
import com.example.bookshelf.data.BookRepository
import kotlinx.coroutines.launch

class BookshelfViewModel : ViewModel() {
    private val repository = BookRepository()

    var books by mutableStateOf<List<Book>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun searchBooks(query: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                books = repository.searchBooks(query)
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }
}