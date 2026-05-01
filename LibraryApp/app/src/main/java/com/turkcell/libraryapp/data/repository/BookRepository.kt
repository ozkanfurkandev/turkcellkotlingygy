package com.turkcell.libraryapp.data.repository

import com.turkcell.libraryapp.data.model.Book
import com.turkcell.libraryapp.data.supabase.supabase
import io.github.jan.supabase.postgrest.postgrest

class BookRepository {
    suspend fun getAllBooks(): Result<List<Book>> = runCatching {
        supabase.postgrest["books"]
            .select()
            .decodeList<Book>()
    }

    suspend fun getBookById(id: String): Result<Book> = runCatching {
        supabase.postgrest["books"]
            .select { filter { eq("id", id) } }
            .decodeSingle<Book>()
    }

    suspend fun addBook(book: Book): Result<Unit> = runCatching {
        supabase.postgrest["books"].insert(book)
    }

    suspend fun updateBook(id: String, book: Book): Result<Unit> = runCatching {
        supabase.postgrest["books"].update(book) {
            filter { eq("id", id) }
        }
    }

    suspend fun deleteBook(id: String): Result<Unit> = runCatching {
        supabase.postgrest["books"].delete {
            filter { eq("id", id) }
        }
    }

    // title veya author içinde, büyük/küçük harf duyarsız arama
    suspend fun searchBooks(query: String): Result<List<Book>> = runCatching {
        val pattern = "%${query.trim()}%"
        supabase.postgrest["books"]
            .select {
                filter {
                    or {
                        ilike("title", pattern)
                        ilike("author", pattern)
                    }
                }
            }
            .decodeList<Book>()
    }
}
