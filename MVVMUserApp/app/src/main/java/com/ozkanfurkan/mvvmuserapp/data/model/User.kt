package com.ozkanfurkan.mvvmuserapp.data.model

/**
 * JSONPlaceholder Users API'den dönen kullanıcı verisini temsil eder.
 * Ödev gereği sadece gerekli alanlar modele dahil edilmiştir.
 */
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String
)
