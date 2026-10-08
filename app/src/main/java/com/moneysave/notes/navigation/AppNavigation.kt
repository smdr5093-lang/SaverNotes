package com.moneysave.notes.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moneysave.notes.ui.screens.HomeScreen
import com.moneysave.notes.ui.screens.MoneyScreen
import com.moneysave.notes.ui.screens.SavingsScreen
import com.moneysave.notes.ui.screens.NotesScreen
import com.moneysave.notes.ui.screens.SettingsScreen

private data class BottomItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

private val bottomItems = listOf(
    BottomItem("home", "Home", Icons.Default.Home),
    BottomItem("money", "Money", Icons.Default.AccountBalanceWallet),
    BottomItem("savings", "Savings", Icons.Default.Savings),
    BottomItem("notes", "Notes", Icons.Default.Note),
    BottomItem("settings", "Settings", Icons.Default.Settings)
)

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo("home") {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(item.label)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                HomeScreen(paddingValues)
            }

            composable("money") {
                MoneyScreen(paddingValues)
            }

            composable("savings") {
                SavingsScreen(paddingValues)
            }

            composable("notes") {
                NotesScreen(paddingValues)
            }

            composable("settings") {
                SettingsScreen(paddingValues)
            }
        }
    }
}
