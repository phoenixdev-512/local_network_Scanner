package com.example.local_network_scanner

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.local_network_scanner.ui.*
import com.example.local_network_scanner.ui.theme.*
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object Dashboard : Screen("dashboard", "Dashboard", { Icon(Icons.Filled.Home, contentDescription = null) })
    object Network : Screen("network", "Network", { Icon(Icons.Filled.Wifi, contentDescription = null) })
    object Security : Screen("security", "Security", { Icon(Icons.Filled.Security, contentDescription = null) })
    object Activity : Screen("activity", "Activity", { Icon(Icons.Filled.History, contentDescription = null) })
    
    // Legacy routes (still accessible)
    object Firewall : Screen("firewall", "Firewall", { Icon(Icons.Filled.Security, contentDescription = null) })
    object AppRules : Screen("app_rules", "App Rules", { Icon(Icons.Filled.List, contentDescription = null) })
    object ConnectionLog : Screen("connection_log", "Connection Log", { Icon(Icons.Filled.History, contentDescription = null) })
    object Map : Screen("map", "Map", { Icon(Icons.Filled.Map, contentDescription = null) })
    object Wifi : Screen("wifi", "Wi-Fi", { Icon(Icons.Filled.Wifi, contentDescription = null) })
    object Settings : Screen("settings", "Settings", { Icon(Icons.Filled.Settings, contentDescription = null) })
    
    // New screens
    object Profile : Screen("profile", "Profile", { Icon(Icons.Filled.Person, contentDescription = null) })
    object ProfileManagement : Screen("profile_management", "Profile Management", { Icon(Icons.Filled.SupervisorAccount, contentDescription = null) })
    object NetworkManager : Screen("network_manager", "Network Manager", { Icon(Icons.Filled.Router, contentDescription = null) })
    object HelpDocumentation : Screen("help_documentation", "Help & Documentation", { Icon(Icons.Filled.Help, contentDescription = null) })
    object About : Screen("about", "About", { Icon(Icons.Filled.Info, contentDescription = null) })
}

// Bottom navigation items (4 essential tabs)
val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Network,
    Screen.Security,
    Screen.Activity
)

/**
 * Main application composable for SENET
 * 
 * This is the root composable that sets up:
 * - Navigation drawer with app branding and menu
 * - Bottom navigation bar with 4 main sections
 * - Navigation host with all app screens
 * - Screen transitions and animations
 * 
 * The app follows a Material 3 design with a dark theme optimized for
 * network monitoring and security visualization.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetSentryApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = SurfaceDarkGray
            ) {
                NavigationDrawerContent(
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = SurfaceDarkGray
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    bottomNavItems.forEach { screen ->
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
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ElectricBlue,
                                selectedTextColor = ElectricBlue,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = CardBackground
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = Screen.Dashboard.route,
                Modifier.padding(innerPadding)
            ) {
                // New screens with animations
                composable(
                    route = Screen.Dashboard.route,
                    enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(animationSpec = tween(300)) { -it / 2 } },
                    exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(animationSpec = tween(300)) { -it / 2 } }
                ) { DashboardScreen(navController = navController) }
                
                composable(
                    route = Screen.Network.route,
                    enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(animationSpec = tween(300)) { it / 2 } },
                    exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(animationSpec = tween(300)) { it / 2 } }
                ) { NetworkScannerScreen() } // Network scanning with WiFi discovery
                
                composable(
                    route = Screen.Security.route,
                    enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(animationSpec = tween(300)) { it / 2 } },
                    exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(animationSpec = tween(300)) { it / 2 } }
                ) { SecurityScreen() } // Security scan and threat detection
                
                composable(
                    route = Screen.Activity.route,
                    enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(animationSpec = tween(300)) { it / 2 } },
                    exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(animationSpec = tween(300)) { it / 2 } }
                ) { ActivityScreen() } // New Activity screen with 5-minute network stats
                
                // Legacy routes
                composable(Screen.Firewall.route) { FirewallScreen(navController) }
                composable(Screen.AppRules.route) { AppListScreen() }
                composable(Screen.ConnectionLog.route) { EnhancedLogScreen() }
                composable(Screen.Map.route) { MapScreen() }
                composable(Screen.Wifi.route) { WifiScreen() }
                composable(Screen.Settings.route) { EnhancedSettingsScreen() }
                
                // New feature screens with slide transitions
                composable(
                    route = Screen.Profile.route,
                    enterTransition = { slideInHorizontally(animationSpec = tween(400)) { it } + fadeIn(animationSpec = tween(400)) },
                    exitTransition = { slideOutHorizontally(animationSpec = tween(400)) { it } + fadeOut(animationSpec = tween(400)) }
                ) { ProfileScreen() }
                
                composable(
                    route = Screen.ProfileManagement.route,
                    enterTransition = { slideInHorizontally(animationSpec = tween(400)) { it } + fadeIn(animationSpec = tween(400)) },
                    exitTransition = { slideOutHorizontally(animationSpec = tween(400)) { it } + fadeOut(animationSpec = tween(400)) }
                ) { ProfileManagementScreen(navController = navController) }
                
                composable(
                    route = Screen.NetworkManager.route,
                    enterTransition = { slideInHorizontally(animationSpec = tween(400)) { it } + fadeIn(animationSpec = tween(400)) },
                    exitTransition = { slideOutHorizontally(animationSpec = tween(400)) { it } + fadeOut(animationSpec = tween(400)) }
                ) { NetworkManagerScreen() }
                
                composable(
                    route = Screen.HelpDocumentation.route,
                    enterTransition = { slideInHorizontally(animationSpec = tween(400)) { it } + fadeIn(animationSpec = tween(400)) },
                    exitTransition = { slideOutHorizontally(animationSpec = tween(400)) { it } + fadeOut(animationSpec = tween(400)) }
                ) { HelpDocumentationScreen(navController = navController) }
                
                composable(
                    route = Screen.About.route,
                    enterTransition = { slideInHorizontally(animationSpec = tween(400)) { it } + fadeIn(animationSpec = tween(400)) },
                    exitTransition = { slideOutHorizontally(animationSpec = tween(400)) { it } + fadeOut(animationSpec = tween(400)) }
                ) { AboutScreen(navController = navController) }
            }
        }
    }
}

/**
 * Navigation drawer content with app branding and menu items
 * 
 * Features:
 * - Header with app logo (ic_launcher) and SENET branding
 * - Navigation items for Profile, Network Manager, Settings, Help, and About
 * - Footer with app version information
 * - Material 3 styling with gradient background
 * 
 * @param onNavigate Callback for navigation actions
 */
@Composable
private fun NavigationDrawerContent(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepNavy, SurfaceDarkGray)
                )
            )
            .padding(vertical = 24.dp)
    ) {
        // Header with profile
        DrawerHeader()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        HorizontalDivider(color = CardBackground)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Navigation items
        DrawerNavigationItem(
            icon = Icons.Filled.SupervisorAccount,
            label = "Profile Management",
            onClick = { onNavigate(Screen.ProfileManagement.route) }
        )
        DrawerNavigationItem(
            icon = Icons.Filled.Router,
            label = "Network Manager",
            onClick = { onNavigate(Screen.NetworkManager.route) }
        )
        DrawerNavigationItem(
            icon = Icons.Filled.Settings,
            label = "Settings & Preferences",
            onClick = { onNavigate(Screen.Settings.route) }
        )
        DrawerNavigationItem(
            icon = Icons.Filled.Help,
            label = "Help & Documentation",
            onClick = { onNavigate(Screen.HelpDocumentation.route) }
        )
        DrawerNavigationItem(
            icon = Icons.Filled.Info,
            label = "About",
            onClick = { onNavigate(Screen.About.route) }
            }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Footer
        Text(
            text = "SENET v1.0",
            style = MaterialTheme.typography.bodySmall,
            color = TextTertiary,
            modifier = Modifier.padding(horizontal = 28.dp)
        )
    }
}

/**
 * Drawer header with app logo and branding
 * 
 * Displays the SENET app logo (from ic_launcher mipmap) in a circular
 * format along with the app name and tagline. This provides consistent
 * branding throughout the navigation experience.
 */
@Composable
private fun DrawerHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App logo as avatar
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = "App logo",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Column {
            Text(
                text = "SENET",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Network Security Monitor",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun DrawerNavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = null) },
        label = { Text(label) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
            unselectedIconColor = TextSecondary,
            unselectedTextColor = TextPrimary,
            selectedContainerColor = CardBackground,
            selectedIconColor = ElectricBlue,
            selectedTextColor = ElectricBlue
        )
    )
}
