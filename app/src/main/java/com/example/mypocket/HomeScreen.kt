package com.example.mypocket

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    username: String,
    dbHelper: DatabaseHelper,
    modifier: Modifier = Modifier,
    onAddMoney: () -> Unit,
    onSendMoney: () -> Unit,
    onViewTransactions: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var balance by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        balance = dbHelper.getBalance(username)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome, $username", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Balance: \$${String.format("%.2f", balance)}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onAddMoney, modifier = Modifier.fillMaxWidth()) {
            Text("Add Money")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onSendMoney, modifier = Modifier.fillMaxWidth()) {
            Text("Send Money")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onViewTransactions, modifier = Modifier.fillMaxWidth()) {
            Text("Transaction History")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Logout", color = MaterialTheme.colorScheme.onError)
        }
    }
}
