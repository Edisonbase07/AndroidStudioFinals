package com.example.mypocket

import android.app.Activity
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.automirrored.filled.Send



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(currentUsername: String, currentActivity: Activity) {
    val context = LocalContext.current

    TopAppBar(
        title = { Text("My Pocket", color = Color.White) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF990000)  // Red background
        ),
        actions = {
            IconButton(onClick = {
                val intent = Intent(context, HomeActivity::class.java)
                intent.putExtra("username", currentUsername)
                context.startActivity(intent)
                currentActivity.finish()
            }) {
                Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
            }

            IconButton(onClick = {
                val intent = Intent(context, AddMoneyActivity::class.java)
                intent.putExtra("username", currentUsername)
                context.startActivity(intent)
                currentActivity.finish()
            }) {
                Icon(Icons.Default.AttachMoney, contentDescription = "Add Money", tint = Color.LightGray)
            }

            IconButton(onClick = {
                val intent = Intent(context, TransactionHistoryActivity::class.java)
                intent.putExtra("username", currentUsername)
                context.startActivity(intent)
                currentActivity.finish()
            }) {
                Icon(Icons.Default.History, contentDescription = "Transaction History", tint = Color.LightGray)
            }

            IconButton(onClick = {
                val intent = Intent(context, SendMoneyActivity::class.java)
                intent.putExtra("username", currentUsername)
                context.startActivity(intent)
                currentActivity.finish()
            }) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send Money", tint = Color.LightGray)
            }

            IconButton(onClick = {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("username", currentUsername)
                context.startActivity(intent)
                currentActivity.finish()
            }) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.LightGray)
            }

        }
    )
}
