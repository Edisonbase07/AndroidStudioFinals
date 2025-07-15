package com.example.mypocket

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("username") ?: ""

        setContent {
            Scaffold(
                topBar = {
                    TopNavigationBar(currentUsername = username, currentActivity = this)
                }
            ) { innerPadding ->
                HomeScreen(
                    username = username,
                    modifier = Modifier.padding(innerPadding),
                    onAddMoney = {
                        val intent = Intent(this, AddMoneyActivity::class.java)
                        intent.putExtra("username", username)
                        startActivity(intent)
                    },
                    onSendMoney = {
                        val intent = Intent(this, SendMoneyActivity::class.java)
                        intent.putExtra("username", username)
                        startActivity(intent)
                    },
                    onViewTransactions = {
                        val intent = Intent(this, TransactionHistoryActivity::class.java)
                        intent.putExtra("username", username)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}


@Composable
fun HomeScreen(
    username: String,
    modifier: Modifier = Modifier,
    onAddMoney: () -> Unit,
    onSendMoney: () -> Unit,
    onViewTransactions: () -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { DatabaseHelper(context) }
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

        // 🚪 Logout Button
        Button(
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Logout", color = MaterialTheme.colorScheme.onError)
        }
    }
}



