package com.turkcell.libraryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.libraryapp.data.model.BorrowRecord
import com.turkcell.libraryapp.data.repository.AuthRepository
import com.turkcell.libraryapp.data.repository.BorrowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class BorrowEvent {
    data class Success(val message: String) : BorrowEvent()
    data class Error(val message: String) : BorrowEvent()
}

class BorrowViewModel : ViewModel() {

    private val borrowRepository = BorrowRepository()
    private val authRepository = AuthRepository()

    private val _records = MutableStateFlow<List<BorrowRecord>>(emptyList())
    val records: StateFlow<List<BorrowRecord>> = _records.asStateFlow()

    val activeRecords: StateFlow<List<BorrowRecord>> = _records
        .map { list -> list.filter { it.returnedAt == null } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val pastRecords: StateFlow<List<BorrowRecord>> = _records
        .map { list -> list.filter { it.returnedAt != null } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isBorrowing = MutableStateFlow(false)
    val isBorrowing: StateFlow<Boolean> = _isBorrowing.asStateFlow()

    private val _event = MutableStateFlow<BorrowEvent?>(null)
    val event: StateFlow<BorrowEvent?> = _event.asStateFlow()

    fun consumeEvent() {
        _event.value = null
    }

    fun loadMyRecords() {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            _isLoading.value = true
            borrowRepository.getMyRecords(userId)
                .onSuccess { _records.value = it }
                .onFailure { _event.value = BorrowEvent.Error(it.message ?: "Kayıtlar yüklenemedi") }
            _isLoading.value = false
        }
    }

    /**
     * @param days 1-5 gün arası.
     */
    fun borrowBook(bookId: String, days: Int, onDone: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            _isBorrowing.value = true
            borrowRepository.borrowBook(bookId, days)
                .onSuccess {
                    _event.value = BorrowEvent.Success("Kitap ödünç alındı. İade tarihi: ${it.dueDate}")
                    loadMyRecords()
                    onDone(true)
                }
                .onFailure {
                    _event.value = BorrowEvent.Error(it.message ?: "Ödünç alma başarısız")
                    onDone(false)
                }
            _isBorrowing.value = false
        }
    }

    fun returnBook(recordId: String, onDone: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            borrowRepository.returnBook(recordId)
                .onSuccess {
                    _event.value = BorrowEvent.Success("Kitap iade edildi.")
                    loadMyRecords()
                    onDone(true)
                }
                .onFailure {
                    _event.value = BorrowEvent.Error(it.message ?: "İade başarısız")
                    onDone(false)
                }
        }
    }
}
