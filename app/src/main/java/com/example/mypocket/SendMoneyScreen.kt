package com.example.mypocket

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun SendMoneyScreen(
    username: String,
    dbHelper: DatabaseHelper,
    modifier: Modifier = Modifier
) {
    var contact by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Send Money",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    label = { Text("Recipient Username") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val amt = amount.toDoubleOrNull()
                        if (contact.trim() == username.trim()) {
                            Toast.makeText(context, "You cannot send money to yourself", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (contact.isNotBlank() && amt != null && amt > 0) {
                            val success = dbHelper.sendMoney(username, contact, amt)
                            if (success) {
                                Toast.makeText(context, "Money Sent", Toast.LENGTH_SHORT).show()
                                contact = ""
                                amount = ""
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
    }
}
