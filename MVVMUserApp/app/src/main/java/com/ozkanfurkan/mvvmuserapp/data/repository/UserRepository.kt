package com.ozkanfurkan.mvvmuserapp.data.repository

import com.ozkanfurkan.mvvmuserapp.data.model.User
import com.ozkanfurkan.mvvmuserapp.data.remote.ApiService
import com.ozkanfurkan.mvvmuserapp.data.remote.RetrofitInstance

/**
 * Veri kaynağı (şimdilik sadece remote API) ile ViewModel arasındaki köprü.
 * Bu soyutlama sayesinde ileride cache/DB eklemek kolaylaşır.
 */
class UserRepository(
    private val api: ApiService = RetrofitInstance.api
) {

    suspend fun getUsers(): List<User> = api.getUsers()
}
