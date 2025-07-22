package com.example.mypocket

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    var recentTransactions by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        balance = dbHelper.getBalance(username)
        recentTransactions = dbHelper.getRecentTransactions(username, limit = 5)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "Hello, $username",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = "Wallet Balance",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "₱300.00",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary // Red accent
                        )
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons (Send, Add, View)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ActionButton(label = "Add Money", onClick = onAddMoney)
            ActionButton(label = "Send", onClick = onSendMoney)
            ActionButton(label = "History", onClick = onViewTransactions)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Recent Transactions",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (recentTransactions.isEmpty()) {
                item {
                    Text("No transactions available.", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                items(recentTransactions) { txn ->
                    val (action, amount, date) = parseTransaction(txn)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = action, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = amount, style = MaterialTheme.typography.bodyLarge)
                            Text(text = date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ActionButton(label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .padding(4.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 4.dp,
            modifier = Modifier.size(60.dp),
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(label.first().toString(), style = MaterialTheme.typography.headlineSmall)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}

