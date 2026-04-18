package com.ozkanfurkan.turkcellintro.data

import com.ozkanfurkan.turkcellintro.di.SupabaseClient
import com.ozkanfurkan.turkcellintro.model.Todo
import io.github.jan.supabase.postgrest.postgrest


class TodoRepository {
    private val db = SupabaseClient.supabase.postgrest
    suspend fun getTodos(): List<Todo> {
        return db.from("todos").select().decodeList()
    }

    suspend fun addTodo(todo: Todo) {
        db.from("todos").insert(todo)
    }

    suspend fun deleteTodo(id: Int) {
        db.from("todos").delete {
            filter { eq("id", value = id)  }
        }
    }
    
}