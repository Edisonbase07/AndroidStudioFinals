// TransactionUtils.kt
package com.example.mypocket

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun parseTransaction(raw: String): Triple<String, String, String> {
    val parts = raw.split(":")
    val timestamp = parts.getOrNull(0)?.toLongOrNull()
    val details = parts.getOrNull(1)?.trim()

    val action = when {
        details?.contains("[add]") == true -> "Added Money"
        details?.contains("[send]") == true -> "Sent Money"
        else -> "Transaction"
    }

    val amount = Regex("""\$(\d+(\.\d+)?)""").find(raw)?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0
    val formattedAmount = NumberFormat.getCurrencyInstance(Locale.US).format(amount)

    val date = timestamp?.let {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        sdf.format(Date(it))
    } ?: "Unknown Date"

    return Triple(action, formattedAmount, "on $date")
}
