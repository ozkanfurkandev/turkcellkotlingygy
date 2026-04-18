package com.ozkanfurkan.mvvmuserapp.viewmodel

import com.ozkanfurkan.mvvmuserapp.data.model.User

/**
 * Kullanıcı listesi ekranının olası üç durumunu temsil eder:
 *  - Loading : İstek devam ediyor
 *  - Success : Veri başarıyla alındı
 *  - Error   : Bir hata oluştu (mesaj ile)
 */
sealed interface UserUiState {
    object Loading : UserUiState
    data class Success(val users: List<User>) : UserUiState
    data class Error(val message: String) : UserUiState
}
