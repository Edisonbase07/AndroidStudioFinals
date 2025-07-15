package com.example.mypocket

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class AddMoneyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("username") ?: ""

        setContent {
            Scaffold(
                topBar = {
                    TopNavigationBar(currentUsername = username, currentActivity = this)
                }
            ) { innerPadding ->
                AddMoneyScreen(
                    username = username,
                    dbHelper = DatabaseHelper(this),
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun AddMoneyScreen(username: String, dbHelper: DatabaseHelper, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var amount by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Add Money", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val amt = amount.toDoubleOrNull()
                if (amt != null && amt > 0) {
                    dbHelper.addMoney(username, amt)
                    Toast.makeText(context, "Money Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Invalid Amount", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add")
        }
    }
}
