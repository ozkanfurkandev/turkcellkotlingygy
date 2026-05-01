package com.turkcell.libraryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.libraryapp.data.model.Book
import com.turkcell.libraryapp.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {
    private val repository = BookRepository()

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository
                .getAllBooks()
                .onSuccess { _books.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun searchBooks(query: String) {
        if (query.isBlank()) {
            loadBooks()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            repository
                .searchBooks(query)
                .onSuccess { _books.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun updateBook(id: String, book: Book) {
        viewModelScope.launch {
            repository
                .updateBook(id, book)
                .onSuccess { loadBooks() }
                .onFailure { _error.value = it.message }
        }
    }

    fun deleteBook(id: String) {
        viewModelScope.launch {
            repository
                .deleteBook(id)
                .onSuccess { _books.value = _books.value.filterNot { it.id == id } }
                .onFailure { _error.value = it.message }
        }
    }
}
