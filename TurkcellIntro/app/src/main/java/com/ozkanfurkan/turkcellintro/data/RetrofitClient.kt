package com.ozkanfurkan.turkcellintro.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



// class -> object =>
// API isteği atacak 10 tane eleman?
// 1 tane eleman?
object RetrofitClient
{
    // Bu objeyi yalnızca ilk ihtiyaç duyulduğunda oluştur.
    val api: ToDoApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ToDoApiService::class.java)
    }
}