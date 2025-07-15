package com.example.mypocket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class TransactionHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("username") ?: ""

        setContent {
            val dbHelper = DatabaseHelper(this)

            Scaffold(
                topBar = {
                    TopNavigationBar(currentUsername = username, currentActivity = this)
                }
            ) { innerPadding ->
                TransactionHistoryScreen(
                    username = username,
                    dbHelper = dbHelper,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun TransactionHistoryScreen(
    username: String,
    dbHelper: DatabaseHelper,
    modifier: Modifier = Modifier
) {
    var transactions by remember { mutableStateOf(listOf<String>()) }

    // Load transaction history
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
                    HorizontalDivider()
                }
            }
        }
    }
}
