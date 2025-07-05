package com.example.mypocket

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mypocket.ui.theme.MyPocketTheme
import androidx.compose.ui.platform.LocalContext


class ChangePasswordActivity : ComponentActivity() {
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = DatabaseHelper(this)

        val username = intent.getStringExtra("username")

        setContent {
            MyPocketTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    if (username != null) {
                        ChangePasswordScreen(
                            username = username,
                            dbHelper = db,
                            onPasswordChanged = {
                                Toast.makeText(this, "Password updated!", Toast.LENGTH_SHORT).show()
                                // Go back to login
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        )
                    } else {
                        Text("Error: Username missing.")
                    }
                }
            }
        }
    }
}

@Composable
fun ChangePasswordScreen(
    username: String,
    dbHelper: DatabaseHelper,
    onPasswordChanged: () -> Unit
) {
    val context = LocalContext.current
    var newPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Change Password", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (newPassword.isNotBlank()) {
                val updated = dbHelper.updatePassword(username, newPassword)
                if (updated) {
                    Toast.makeText(context, "Password updated!", Toast.LENGTH_SHORT).show()
                    onPasswordChanged()
                } else {
                    Toast.makeText(context, "Failed to update password.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Password cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Update Password")
        }
    }
}

