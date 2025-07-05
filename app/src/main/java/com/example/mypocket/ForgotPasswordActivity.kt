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
import androidx.compose.ui.unit.dp
import com.example.mypocket.ui.theme.MyPocketTheme
import androidx.compose.ui.platform.LocalContext

class ForgotPasswordActivity : ComponentActivity() {
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHelper(this)

        setContent {
            MyPocketTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ForgotPasswordScreen(
                        dbHelper = db,
                        onNext = { username ->
                            // Send username to ChangePasswordActivity
                            val intent = Intent(this, ChangePasswordActivity::class.java)
                            intent.putExtra("username", username)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    dbHelper: DatabaseHelper,
    onNext: (String) -> Unit
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Forgot Password", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val email = dbHelper.getEmail(username)
            if (email != null) {
                Toast.makeText(context, "Email: $email", Toast.LENGTH_LONG).show()
                onNext(username)
            } else {
                Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Next")
        }
    }
}
