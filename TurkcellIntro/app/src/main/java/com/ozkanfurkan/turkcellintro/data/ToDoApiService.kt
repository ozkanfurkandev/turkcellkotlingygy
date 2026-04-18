package com.ozkanfurkan.turkcellintro.data

import com.ozkanfurkan.turkcellintro.model.Todo
import retrofit2.http.GET

interface ToDoApiService {
    @GET("todos")
    suspend fun getToDos(): List<Todo> // suspend olmazsa fonksiyon bitene kadar thread'i kitler.
    //UI'da kilitlenme yaşamamak için suspend kullanılır.
}

