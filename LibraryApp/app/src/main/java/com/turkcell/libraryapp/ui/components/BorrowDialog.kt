package com.turkcell.libraryapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.turkcell.libraryapp.data.model.Book
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BorrowDialog(
    book: Book,
    isBorrowing: Boolean,
    onConfirm: (days: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedDays by remember { mutableIntStateOf(5) }
    val dueDate = remember(selectedDays) {
        val cal = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, selectedDays)
        }
        SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("tr")).format(cal.time)
    }

    AlertDialog(
        onDismissRequest = { if (!isBorrowing) onDismiss() },
        title = {
            Text(
                text = "Ödünç Al",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Kaç gün almak istersiniz? (Maks. 5 gün)",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    (1..5).forEach { day ->
                        FilterChip(
                            selected = selectedDays == day,
                            onClick = { if (!isBorrowing) selectedDays = day },
                            label = { Text("$day g") },
                            colors = FilterChipDefaults.filterChipColors()
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "İade Tarihi",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = dueDate,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedDays) },
                enabled = !isBorrowing
            ) {
                Text(if (isBorrowing) "İşleniyor..." else "Onayla")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isBorrowing
            ) {
                Text("Vazgeç")
            }
        }
    )
}
