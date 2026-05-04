package com.turkcell.libraryapp.data.repository

import com.turkcell.libraryapp.data.model.BorrowRecord
import com.turkcell.libraryapp.data.supabase.supabase
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class BorrowRepository {

    /**
     * Atomik ödünç alma. Stok kontrolü, kilit ve azaltma DB tarafında borrow_book RPC ile yapılır.
     * @param days 1-5 gün arası
     */
    suspend fun borrowBook(bookId: String, days: Int): Result<BorrowRecord> = runCatching {
        require(days in 1..5) { "Kiralama süresi 1-5 gün arası olmalıdır." }

        val params = buildJsonObject {
            put("p_book_id", bookId)
            put("p_days", days)
        }

        supabase.postgrest
            .rpc("borrow_book", params)
            .decodeSingle<BorrowRecord>()
    }

    /**
     * Verilen öğrenciye ait tüm kayıtları, kitap detaylarıyla birlikte getirir.
     * En yeni kayıt en üstte. (Aktif/pasif ayrımı UI tarafında yapılır.)
     */
    suspend fun getMyRecords(studentId: String): Result<List<BorrowRecord>> = runCatching {
        supabase.postgrest["borrow_records"]
            .select(Columns.raw("*, books(*)")) {
                filter { eq("student_id", studentId) }
                order("borrowed_at", Order.DESCENDING)
            }
            .decodeList<BorrowRecord>()
    }

    /**
     * İade. Atomik şekilde stok arttırılır.
     */
    suspend fun returnBook(recordId: String): Result<BorrowRecord> = runCatching {
        val params = buildJsonObject {
            put("p_record_id", recordId)
        }

        supabase.postgrest
            .rpc("return_book", params)
            .decodeSingle<BorrowRecord>()
    }
}
