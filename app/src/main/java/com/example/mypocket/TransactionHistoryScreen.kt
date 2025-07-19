package com.example.mypocket

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TransactionHistoryScreen(
    username: String,
    dbHelper: DatabaseHelper,
    modifier: Modifier = Modifier
) {
    var transactions by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {
        transactions = dbHelper.getTransactions(username)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Transaction History", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isEmpty()) {
            Text("No transactions yet.")
        } else {
            LazyColumn {
                items(transactions) { transaction ->
                    Text(transaction)
                    Divider()
                }
            }
        }
    }
}
