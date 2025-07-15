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

class SendMoneyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("username") ?: ""

        setContent {
            Scaffold(
                topBar = {
                    TopNavigationBar(currentUsername = username, currentActivity = this)
                }
            ) { innerPadding ->
                SendMoneyScreen(
                    username = username,
                    dbHelper = DatabaseHelper(this),
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun SendMoneyScreen(username: String, dbHelper: DatabaseHelper, modifier: Modifier = Modifier) {
    var contact by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Send Money", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = contact,
            onValueChange = { contact = it },
            label = { Text("Contact Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

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
                if (contact.isNotBlank() && amt != null && amt > 0) {
                    val success = dbHelper.sendMoney(username, contact, amt)
                    if (success) {
                        Toast.makeText(context, "Money Sent", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to Send Money", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send")
        }
    }
}
