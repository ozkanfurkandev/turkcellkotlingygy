package com.ozkanfurkan.turkcellintro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ozkanfurkan.turkcellintro.data.TodoRepository
import com.ozkanfurkan.turkcellintro.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ToDoListViewModel : ViewModel()
{
    private val repository = TodoRepository()

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchTodos()
    }

    fun fetchTodos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = repository.getTodos()
                _todos.value = result
            } catch (e: Exception){
                _error.value = e.message ?: "Bir hata oluştu."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteTodo(id)
                fetchTodos()
            } catch (e: Exception) {
                _error.value = e.message ?: "Bir hata oluştu."
            }
        }
    }

    fun addTodo(title: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.addTodo(Todo(id = 0, title = title))
                fetchTodos()
                onComplete()
            } catch (e: Exception) {
                _error.value = e.message ?: "Bir hata oluştu."
            }
        }
    }
}