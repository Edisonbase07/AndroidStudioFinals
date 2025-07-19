package com.example.mypocket

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Send

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    currentUsername: String,
    navController: NavController,
    currentRoute: String
) {
    // Extract the base route by removing anything after a slash
    val isActive: (String) -> Boolean = { route ->
        currentRoute.startsWith("$route/")
    }

    TopAppBar(
        title = { Text("My Pocket", color = Color.White) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF990000)),
        actions = {
            IconButton(onClick = {
                navController.navigate("home/$currentUsername") {
                    popUpTo("home/$currentUsername") { inclusive = true }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (isActive("home")) Color.White else Color.LightGray
                )
            }

            IconButton(onClick = {
                navController.navigate("addMoney/$currentUsername")
            }) {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = "Add Money",
                    tint = if (isActive("addMoney")) Color.White else Color.LightGray
                )
            }

            IconButton(onClick = {
                navController.navigate("transactionHistory/$currentUsername")
            }) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Transaction History",
                    tint = if (isActive("transactionHistory")) Color.White else Color.LightGray
                )
            }

            IconButton(onClick = {
                navController.navigate("sendMoney/$currentUsername")
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send Money",
                    tint = if (isActive("sendMoney")) Color.White else Color.LightGray
                )
            }

            IconButton(onClick = {
                navController.navigate("profile/$currentUsername")
            }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = if (isActive("profile")) Color.White else Color.LightGray
                )
            }
        }
    )
}
