package com.example.bookshelf

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookshelfViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<BookshelfUiState>(BookshelfUiState.Initial)
    val uiState: StateFlow<BookshelfUiState> = _uiState

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun searchBooks() {
        viewModelScope.launch {
            _uiState.value = BookshelfUiState.Loading
            try {
                val response = RetrofitInstance.api.searchBooks(_searchQuery.value)
                val books = response.items
                if (books.isEmpty()) {
                    _uiState.value = BookshelfUiState.Empty
                } else {
                    _uiState.value = BookshelfUiState.Success(books)
                }
            } catch (e: Exception) {
                _uiState.value = BookshelfUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun resetSearch() {
        _searchQuery.value = ""
        _uiState.value = BookshelfUiState.Initial
    }
}