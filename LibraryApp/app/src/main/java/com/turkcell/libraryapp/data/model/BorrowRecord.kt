package com.turkcell.libraryapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BorrowRecord (
    val id: String = "",
    @SerialName("student_id") val studentId: String,
    @SerialName("book_id") val bookId: String,
    @SerialName("borrowed_at") val borrowedAt: String = "",
    @SerialName("due_date") val dueDate: String = "",
    @SerialName("returned_at") val returnedAt: String? = null,
    // Supabase embed: select("*, books(*)") sorgusunda doluyor.
    val books: Book? = null
)
