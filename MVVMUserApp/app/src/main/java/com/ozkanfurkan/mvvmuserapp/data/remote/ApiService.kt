package com.ozkanfurkan.mvvmuserapp.data.remote

import com.ozkanfurkan.mvvmuserapp.data.model.User
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<User>
}
