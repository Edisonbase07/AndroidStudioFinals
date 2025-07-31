package com.example.mypocket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.mypocket.ui.theme.MyPocketTheme

class MainActivity : ComponentActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHelper(this)

        setContent {
            MyPocketTheme {
                val navController = rememberNavController()

                // Observe current route
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route ?: ""

                // Routes that should not show the TopAppBar
                val hideTopBarRoutes = listOf(
                    "login", "register", "forgotPassword", "changePassword/{username}"
                )

                // Get current username from backstack entry arguments if available
                val currentUsername = currentBackStackEntry?.arguments?.getString("username") ?: ""


                Scaffold(
                    topBar = {
                        if (!hideTopBarRoutes.any { currentRoute.startsWith(it.substringBefore("/{")) }) {
                            TopNavigationBar(
                                currentUsername = currentUsername,
                                navController = navController,
                                currentRoute = currentRoute
                            )

                        }
                    }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = "login",
                    ) {
                        composable("login") {
                            LoginScreen(
                                dbHelper = db,
                                onLoginSuccess = { username ->
                                    navController.navigate("home/$username") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate("register")
                                },
                                onNavigateToForgot = {
                                    navController.navigate("forgotPassword")
                                }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                dbHelper = db,
                                onRegisterSuccess = {
                                    navController.popBackStack("login", false)
                                }
                            )
                        }
                        composable("forgotPassword") {
                            ForgotPasswordScreen(
                                dbHelper = db,
                                onNext = { username ->
                                    navController.navigate("changePassword/$username")
                                }
                            )
                        }
                        composable(
                            route = "changePassword/{username}",
                            arguments = listOf(navArgument("username") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val username = backStackEntry.arguments?.getString("username") ?: ""
                            ChangePasswordScreen(
                                username = username,
                                dbHelper = db,
                                onPasswordChanged = {
                                    navController.navigate("login") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(
                            route = "home/{username}",
                            arguments = listOf(navArgument("username") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val username = backStackEntry.arguments?.getString("username") ?: ""
                            HomeScreen(
                                username = username,
                                dbHelper = db,
                                onAddMoney = { navController.navigate("addMoney/$username") },
                                onSendMoney = { navController.navigate("sendMoney/$username") },
                                onViewTransactions = { navController.navigate("transactionHistory/$username") },
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable(
                            route = "addMoney/{username}",
                            arguments = listOf(navArgument("username") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val username = backStackEntry.arguments?.getString("username") ?: ""
                            AddMoneyScreen(
                                username = username,
                                dbHelper = db,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable(
                            route = "sendMoney/{username}",
                            arguments = listOf(navArgument("username") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val username = backStackEntry.arguments?.getString("username") ?: ""
                            SendMoneyScreen(
                                username = username,
                                dbHelper = db,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable(
                            route = "transactionHistory/{username}",
                            arguments = listOf(navArgument("username") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val username = backStackEntry.arguments?.getString("username") ?: ""
                            TransactionHistoryScreen(
                                username = username,
                                dbHelper = db,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable(
                            route = "profile/{username}",
                            arguments = listOf(navArgument("username") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val username = backStackEntry.arguments?.getString("username") ?: ""
                            ProfileScreen(
                                username = username,
                                dbHelper = db,
                                modifier = Modifier.padding(innerPadding),
                                navController = navController  // Pass navController if you want to handle logout or navigation in profile
                            )
                        }
                    }
                }
            }
        }
    }
}
