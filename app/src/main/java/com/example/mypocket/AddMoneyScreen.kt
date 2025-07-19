package com.example.mypocket

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

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
                    val success = dbHelper.addMoney(username, amt)
                    if (success) {
                        Toast.makeText(context, "Money Added", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to add money", Toast.LENGTH_SHORT).show()
                    }
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
