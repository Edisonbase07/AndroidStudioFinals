package com.example.mypocket

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    currentUsername: String,
    navController: NavController,
    currentRoute: String
) {
    val colorScheme = MaterialTheme.colorScheme

    val isActive: (String) -> Boolean = { route ->
        currentRoute == route || currentRoute.startsWith("$route/")
    }

    TopAppBar(
        title = {
            Text(
                "My Pocket",
                color = colorScheme.onPrimary
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.primary,
            titleContentColor = colorScheme.onPrimary
        ),
        actions = {
            TopNavIcon(
                navController, "home/$currentUsername", isActive("home"),
                Icons.Default.Home, "Home"
            )

            TopNavIcon(
                navController, "addMoney/$currentUsername", isActive("addMoney"),
                Icons.Default.AttachMoney, "Add Money"
            )

            TopNavIcon(
                navController, "transactionHistory/$currentUsername", isActive("transactionHistory"),
                Icons.Default.History, "Transaction History"
            )

            TopNavIcon(
                navController, "sendMoney/$currentUsername", isActive("sendMoney"),
                Icons.AutoMirrored.Filled.Send, "Send Money"
            )

            TopNavIcon(
                navController, "profile/$currentUsername", isActive("profile"),
                Icons.Default.Person, "Profile"
            )
        }
    )
}

@Composable
fun TopNavIcon(
    navController: NavController,
    route: String,
    isActive: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String
) {
    IconButton(onClick = {
        navController.navigate(route)
    }) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = if (isActive) Color.White else Color.LightGray
        )
    }
}
