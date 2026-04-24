package com.turkcell.libraryapp.data.repository

import kotlinx.coroutines.delay

class AuthRepository {

    suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        delay(timeMillis = 2000) //dışarıya istek atıyormuş gibi

        val storedPassword = users[email.trim().lowercase()]
            ?: throw Exception("Kullanıcı bulunamadı")

        if (storedPassword != password)
            throw Exception("Şifre hatalı")
    }

    suspend fun signUp(email: String, password: String): Result<Unit> = runCatching {
        delay(timeMillis = 2000) //dışarıya istek atıyormuş gibi

        val key = email.trim().lowercase() //kullanıcı büyük/küçük harf farkıyla giriş yapamasın diye.
        if (key.isBlank())
            throw Exception("E-posta boş olamaz")
        if (password.isBlank())
            throw Exception("Şifre boş olamaz")
        if (users.containsKey(key))
            throw Exception("Bu e-posta zaten kayıtlı")

        users[key] = password
    }

    companion object {
        private val users: MutableMap<String, String> = mutableMapOf()
    }
}
