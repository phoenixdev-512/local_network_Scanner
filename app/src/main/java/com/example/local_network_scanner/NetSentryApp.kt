package com.example.local_network_scanner

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.local_network_scanner.ui.AppListScreen
import com.example.local_network_scanner.ui.FirewallScreen
import com.example.local_network_scanner.ui.LogScreen
import com.example.local_network_scanner.ui.MapScreen
import com.example.local_network_scanner.ui.SettingsScreen
import com.example.local_network_scanner.ui.WifiScreen

sealed class Screen(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object Firewall : Screen("firewall", "Firewall", { Icon(Icons.Filled.Security, contentDescription = null) })
    object AppRules : Screen("app_rules", "App Rules", { Icon(Icons.Filled.List, contentDescription = null) })
    object ConnectionLog : Screen("connection_log", "Connection Log", { Icon(Icons.Filled.History, contentDescription = null) })
    object Map : Screen("map", "Map", { Icon(Icons.Filled.Map, contentDescription = null) })
    object Wifi : Screen("wifi", "Wi-Fi", { Icon(Icons.Filled.Wifi, contentDescription = null) })
    object Settings : Screen("settings", "Settings", { Icon(Icons.Filled.Settings, contentDescription = null) })
}

val items = listOf(
    Screen.Firewall,
    Screen.AppRules,
    Screen.ConnectionLog,
    Screen.Map,
    Screen.Wifi
)

@Composable
fun NetSentryApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { screen.icon() },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Firewall.route, Modifier.padding(innerPadding)) {
            composable(Screen.Firewall.route) { FirewallScreen(navController) }
            composable(Screen.AppRules.route) { AppListScreen() }
            composable(Screen.ConnectionLog.route) { LogScreen() }
            composable(Screen.Map.route) { MapScreen() }
            composable(Screen.Wifi.route) { WifiScreen() }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
        }
    }
}
