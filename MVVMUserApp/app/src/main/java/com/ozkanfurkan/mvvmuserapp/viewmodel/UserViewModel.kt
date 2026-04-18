package com.ozkanfurkan.mvvmuserapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ozkanfurkan.mvvmuserapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    init {
        fetchUsers()
    }

    /**
     * API üzerinden kullanıcıları çeker ve [uiState]'i günceller.
     * Hata yönetimi try/catch ile yapılır; istek başarısız olursa
     * [UserUiState.Error] durumuna geçilir.
     */
    fun fetchUsers() {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            try {
                val users = repository.getUsers()
                _uiState.value = UserUiState.Success(users)
            } catch (e: Exception) {
                _uiState.value = UserUiState.Error(
                    message = e.localizedMessage ?: "Bilinmeyen bir hata oluştu."
                )
            }
        }
    }
}
