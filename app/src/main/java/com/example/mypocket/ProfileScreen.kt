package com.example.mypocket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun ProfileScreen(
    username: String,
    dbHelper: DatabaseHelper,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            ProfileItem(label = "Full Name", value = fullName)
            ProfileItem(label = "Email", value = email)
            ProfileItem(label = "Address", value = address)
            ProfileItem(label = "Phone Number", value = phone)
            ProfileItem(label = "Gender", value = gender)
            ProfileItem(label = "Date of Birth", value = dob)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Logout", color = MaterialTheme.colorScheme.onError)
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
