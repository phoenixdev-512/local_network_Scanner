package com.example.local_network_scanner.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.local_network_scanner.data.db.UserRole
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.viewmodel.SettingsViewModel

/**
 * Enhanced Settings screen with organized sections
 * Includes appearance, security, notifications, and admin tools
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedSettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val blockAllByDefault by viewModel.blockAllByDefault.collectAsState()
    val dnsSettings by viewModel.dnsSettings.collectAsState()
    val enableWeeklySummary by viewModel.enableWeeklySummary.collectAsState()
    
    // Mock user role - should come from ProfileViewModel
    val userRole = UserRole.ADMIN
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepNavy, GradientMiddle, TrueBlack)
                )
            )
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Settings", color = TextPrimary) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            ),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            }
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Appearance Section
            item {
                SettingsSection(
                    title = "Appearance",
                    icon = Icons.Filled.Palette,
                    items = listOf(
                        SettingItem.Toggle(
                            "Dark Mode",
                            "Use dark theme throughout the app",
                            Icons.Filled.DarkMode,
                            true,
                            {}
                        ),
                        SettingItem.Navigation(
                            "Theme Selection",
                            "Speedtest, Classic, Minimal",
                            Icons.Filled.ColorLens,
                            {}
                        ),
                        SettingItem.Navigation(
                            "Font Size",
                            "Adjust text size for better readability",
                            Icons.Filled.FormatSize,
                            {}
                        )
                    )
                )
            }
            
            // Security & Privacy Section
            item {
                SettingsSection(
                    title = "Security & Privacy",
                    icon = Icons.Filled.Security,
                    items = listOf(
                        SettingItem.Toggle(
                            "VPN Auto-Start",
                            "Start VPN when device boots",
                            Icons.Filled.VpnKey,
                            false,
                            {}
                        ),
                        SettingItem.Toggle(
                            "Block All by Default",
                            "Block all connections by default",
                            Icons.Filled.Block,
                            blockAllByDefault,
                            { viewModel.setBlockAllByDefault(!blockAllByDefault) }
                        ),
                        SettingItem.Navigation(
                            "DNS Provider",
                            dnsSettings.provider,
                            Icons.Filled.Dns,
                            {}
                        ),
                        SettingItem.Toggle(
                            "Privacy Mode",
                            "Hide sensitive information in screenshots",
                            Icons.Filled.PrivacyTip,
                            false,
                            {}
                        )
                    )
                )
            }
            
            // Notifications Section
            item {
                SettingsSection(
                    title = "Notifications",
                    icon = Icons.Filled.Notifications,
                    items = listOf(
                        SettingItem.Toggle(
                            "Threat Alerts",
                            "Get notified of security threats",
                            Icons.Filled.Warning,
                            true,
                            {}
                        ),
                        SettingItem.Toggle(
                            "Connection Logs",
                            "Notify on new connections",
                            Icons.Filled.NotificationsActive,
                            false,
                            {}
                        ),
                        SettingItem.Toggle(
                            "Weekly Summary",
                            "Receive weekly usage reports",
                            Icons.Filled.Assessment,
                            enableWeeklySummary,
                            { viewModel.setEnableWeeklySummary(!enableWeeklySummary) }
                        )
                    )
                )
            }
            
            // Advanced Section
            item {
                SettingsSection(
                    title = "Advanced",
                    icon = Icons.Filled.Settings,
                    items = listOf(
                        SettingItem.Toggle(
                            "Debug Logging",
                            "Enable detailed logs for troubleshooting",
                            Icons.Filled.BugReport,
                            false,
                            {}
                        ),
                        SettingItem.Navigation(
                            "Export Settings",
                            "Backup your configuration",
                            Icons.Filled.Download,
                            {}
                        ),
                        SettingItem.Navigation(
                            "Import Settings",
                            "Restore from backup",
                            Icons.Filled.Upload,
                            {}
                        ),
                        SettingItem.Action(
                            "Reset to Defaults",
                            "Restore default settings",
                            Icons.Filled.RestartAlt,
                            {}
                        )
                    )
                )
            }
            
            // Admin-Only Section
            if (userRole == UserRole.ADMIN) {
                item {
                    SettingsSection(
                        title = "Admin Tools",
                        icon = Icons.Filled.AdminPanelSettings,
                        items = listOf(
                            SettingItem.Navigation(
                                "User Management",
                                "Manage users and permissions",
                                Icons.Filled.SupervisorAccount,
                                {}
                            ),
                            SettingItem.Navigation(
                                "Global Policies",
                                "Network-wide security rules",
                                Icons.Filled.Policy,
                                {}
                            ),
                            SettingItem.Navigation(
                                "Audit Logs",
                                "View system activity logs",
                                Icons.Filled.History,
                                {}
                            ),
                            SettingItem.Navigation(
                                "System Diagnostics",
                                "Check system health",
                                Icons.Filled.Insights,
                                {}
                            )
                        )
                    )
                }
            }
            
            // About Section
            item {
                SettingsSection(
                    title = "About",
                    icon = Icons.Filled.Info,
                    items = listOf(
                        SettingItem.Info("Version", "1.0.0", Icons.Filled.AppSettingsAlt),
                        SettingItem.Navigation(
                            "Help & Documentation",
                            "Get help using NetSentry",
                            Icons.Filled.Help,
                            {}
                        ),
                        SettingItem.Navigation(
                            "Privacy Policy",
                            "Read our privacy policy",
                            Icons.Filled.Description,
                            {}
                        ),
                        SettingItem.Navigation(
                            "Open Source Licenses",
                            "View third-party licenses",
                            Icons.Filled.Code,
                            {}
                        )
                    )
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    items: List<SettingItem>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = ElectricBlue,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            items.forEachIndexed { index, item ->
                when (item) {
                    is SettingItem.Toggle -> SettingToggleRow(item)
                    is SettingItem.Navigation -> SettingNavigationRow(item)
                    is SettingItem.Action -> SettingActionRow(item)
                    is SettingItem.Info -> SettingInfoRow(item)
                }
                
                if (index < items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = CardBackground
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingToggleRow(item: SettingItem.Toggle) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                item.icon,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
            }
        }
        Switch(
            checked = item.checked,
            onCheckedChange = { item.onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = VibrантGreen,
                checkedTrackColor = VibrантGreen.copy(alpha = 0.5f),
                uncheckedThumbColor = TextTertiary,
                uncheckedTrackColor = CardBackground
            )
        )
    }
}

@Composable
private fun SettingNavigationRow(item: SettingItem.Navigation) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                item.icon,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
                Text(
                    text = item.value,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
            }
        }
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = TextTertiary
        )
    }
}

@Composable
private fun SettingActionRow(item: SettingItem.Action) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                item.icon,
                contentDescription = null,
                tint = WarningOrange,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = WarningOrange
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
            }
        }
    }
}

@Composable
private fun SettingInfoRow(item: SettingItem.Info) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                item.icon,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
        }
        Text(
            text = item.value,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

// Setting item types
sealed class SettingItem {
    data class Toggle(
        val label: String,
        val description: String,
        val icon: ImageVector,
        val checked: Boolean,
        val onToggle: () -> Unit
    ) : SettingItem()
    
    data class Navigation(
        val label: String,
        val value: String,
        val icon: ImageVector,
        val onClick: () -> Unit
    ) : SettingItem()
    
    data class Action(
        val label: String,
        val description: String,
        val icon: ImageVector,
        val onClick: () -> Unit
    ) : SettingItem()
    
    data class Info(
        val label: String,
        val value: String,
        val icon: ImageVector
    ) : SettingItem()
}
