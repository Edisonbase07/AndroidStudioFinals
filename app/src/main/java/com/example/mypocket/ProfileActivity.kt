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

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("username") ?: ""

        setContent {
            Scaffold(
                topBar = {
                    TopNavigationBar(currentUsername = username, currentActivity = this)
                }
            ) { innerPadding ->
                ProfileScreen(
                    username = username,
                    dbHelper = DatabaseHelper(this),
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(username: String, dbHelper: DatabaseHelper, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val userDetails = dbHelper.getUserProfile(username)

    val fullName = userDetails["full_name"] ?: "N/A"
    val email = userDetails["email"] ?: "N/A"
    val address = userDetails["address"] ?: "N/A"
    val phone = userDetails["phone"] ?: "N/A"
    val gender = userDetails["gender"] ?: "N/A"
    val dob = userDetails["dob"] ?: "N/A"

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Profile", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Full Name: $fullName")
            Text("Email: $email")
            Text("Address: $address")
            Text("Phone Number: $phone")
            Text("Gender: $gender")
            Text("Date of Birth: $dob")

        }

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
