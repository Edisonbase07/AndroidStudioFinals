package com.example.mypocket

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@Composable
fun ProfileScreen(
    username: String,
    dbHelper: DatabaseHelper,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // Load user details from DB
    val userDetails by remember(username) {
        mutableStateOf(dbHelper.getUserProfile(username))
    }

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
                // Logout: navigate back to login and clear backstack
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Logout", color = MaterialTheme.colorScheme.onError)
        }
    }
}
