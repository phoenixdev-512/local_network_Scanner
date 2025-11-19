package com.example.local_network_scanner.ui

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.local_network_scanner.data.db.UserRole
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.viewmodel.ProfileViewModel
import com.example.local_network_scanner.ui.viewmodel.SettingsViewModel

/**
 * Enhanced Settings Screen
 * Complete settings with appearance, security, notifications, and admin options
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedSettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val blockAllByDefault by settingsViewModel.blockAllByDefault.collectAsState()
    val dnsSettings by settingsViewModel.dnsSettings.collectAsState()
    val enableWeeklySummary by settingsViewModel.enableWeeklySummary.collectAsState()
    val currentProfile by profileViewModel.currentProfile.collectAsState()
    
    // Appearance settings
    var darkModeEnabled by remember { mutableStateOf(true) }
    var fontSize by remember { mutableStateOf(FontSize.MEDIUM) }
    var selectedTheme by remember { mutableStateOf(AppTheme.DARK) }
    
    // Security settings
    var autoStartVpn by remember { mutableStateOf(false) }
    var defaultFirewall by remember { mutableStateOf(FirewallBehavior.ALLOW_ALL) }
    
    // Notification settings
    var notifyThreats by remember { mutableStateOf(true) }
    var notifyConnections by remember { mutableStateOf(false) }
    var notifySuspiciousApps by remember { mutableStateOf(true) }
    
    val isAdmin = currentProfile?.role == UserRole.ADMIN
    
    var showResetDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepNavy, GradientMiddle, TrueBlack)
                )
            )
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Settings",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Appearance Section
            item {
                SettingsSectionHeader("Appearance", Icons.Default.Palette)
            }
            
            item {
                SettingsCard {
                    Column {
                        SettingsSwitchRow(
                            title = "Dark Mode",
                            description = "Use dark theme across the app",
                            checked = darkModeEnabled,
                            onCheckedChange = { darkModeEnabled = it },
                            icon = Icons.Default.DarkMode
                        )
                        
                        HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                        
                        SettingsSelectionRow(
                            title = "Font Size",
                            description = fontSize.label,
                            icon = Icons.Default.TextFields,
                            onClick = {
                                fontSize = when (fontSize) {
                                    FontSize.SMALL -> FontSize.MEDIUM
                                    FontSize.MEDIUM -> FontSize.LARGE
                                    FontSize.LARGE -> FontSize.SMALL
                                }
                            }
                        )
                        
                        HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                        
                        SettingsSelectionRow(
                            title = "Theme",
                            description = selectedTheme.label,
                            icon = Icons.Default.ColorLens,
                            onClick = {
                                selectedTheme = when (selectedTheme) {
                                    AppTheme.DARK -> AppTheme.LIGHT
                                    AppTheme.LIGHT -> AppTheme.AUTO
                                    AppTheme.AUTO -> AppTheme.DARK
                                }
                            }
                        )
                    }
                }
            }
            
            // Security & Privacy Section
            item {
                SettingsSectionHeader("Security & Privacy", Icons.Default.Security)
            }
            
            item {
                SettingsCard {
                    Column {
                        SettingsSwitchRow(
                            title = "Auto-Start VPN",
                            description = "Start VPN protection on device boot",
                            checked = autoStartVpn,
                            onCheckedChange = { autoStartVpn = it },
                            icon = Icons.Default.VpnKey
                        )
                        
                        HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                        
                        SettingsSwitchRow(
                            title = "Block All by Default",
                            description = "Block all network traffic by default",
                            checked = blockAllByDefault,
                            onCheckedChange = { settingsViewModel.setBlockAllByDefault(it) },
                            icon = Icons.Default.Block
                        )
                        
                        HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                        
                        SettingsSelectionRow(
                            title = "Default Firewall Behavior",
                            description = defaultFirewall.label,
                            icon = Icons.Default.Shield,
                            onClick = {
                                defaultFirewall = when (defaultFirewall) {
                                    FirewallBehavior.ALLOW_ALL -> FirewallBehavior.BLOCK_ALL
                                    FirewallBehavior.BLOCK_ALL -> FirewallBehavior.SMART
                                    FirewallBehavior.SMART -> FirewallBehavior.ALLOW_ALL
                                }
                            }
                        )
                        
                        HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                        
                        SettingsSelectionRow(
                            title = "DNS Provider",
                            description = dnsSettings.dnsMode,
                            icon = Icons.Default.Dns,
                            onClick = {
                                val modes = listOf("SYSTEM", "CLOUDFLARE", "GOOGLE", "CUSTOM")
                                val currentIndex = modes.indexOf(dnsSettings.dnsMode)
                                val nextMode = modes[(currentIndex + 1) % modes.size]
                                settingsViewModel.setDnsMode(nextMode)
                            }
                        )
                        
                        HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                        
                        SettingsSwitchRow(
                            title = "Secure DNS (DoH)",
                            description = "Enable DNS over HTTPS",
                            checked = dnsSettings.enableSecureDns,
                            onCheckedChange = { settingsViewModel.setEnableSecureDns(it) },
                            icon = Icons.Default.Lock
                        )
                    }
                }
            }
            
            // Notifications Section
            item {
                SettingsSectionHeader("Notifications", Icons.Default.Notifications)
            }
            
            item {
                SettingsCard {
                    Column {
                        SettingsSwitchRow(
                            title = "Threat Notifications",
                            description = "Alert when threats are detected",
                            checked = notifyThreats,
                            onCheckedChange = { notifyThreats = it },
                            icon = Icons.Default.Warning
                        )
                        
                        HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                        
                        SettingsSwitchRow(
                            title = "Connection Logs",
                            description = "Notify on new network connections",
                            checked = notifyConnections,
                            onCheckedChange = { notifyConnections = it },
                            icon = Icons.Default.NetworkCheck
                        )
                        
                        HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                        
                        SettingsSwitchRow(
                            title = "Suspicious Apps",
                            description = "Alert when suspicious apps are found",
                            checked = notifySuspiciousApps,
                            onCheckedChange = { notifySuspiciousApps = it },
                            icon = Icons.Default.BugReport
                        )
                        
                        HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                        
                        SettingsSwitchRow(
                            title = "Weekly Summary",
                            description = "Receive weekly network summary",
                            checked = enableWeeklySummary,
                            onCheckedChange = { settingsViewModel.setEnableWeeklySummary(it) },
                            icon = Icons.Default.CalendarMonth
                        )
                    }
                }
            }
            
            // Data & Storage Section
            item {
                SettingsSectionHeader("Data & Storage", Icons.Default.Storage)
            }
            
            item {
                SettingsCard {
                    Column {
                        SettingsActionRow(
                            title = "Clear Cache",
                            description = "Remove temporary files and cache",
                            icon = Icons.Default.DeleteSweep,
                            onClick = { /* Clear cache */ }
                        )
                        
                        HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                        
                        SettingsActionRow(
                            title = "Reset Preferences",
                            description = "Reset all settings to default",
                            icon = Icons.Default.RestartAlt,
                            onClick = { showResetDialog = true },
                            textColor = WarningOrange
                        )
                    }
                }
            }
            
            // Developer Settings (Admin Only)
            if (isAdmin) {
                item {
                    SettingsSectionHeader("Developer Settings", Icons.Default.DeveloperMode)
                }
                
                item {
                    SettingsCard {
                        Column {
                            SettingsSwitchRow(
                                title = "Debug Mode",
                                description = "Enable detailed logging",
                                checked = false,
                                onCheckedChange = { },
                                icon = Icons.Default.BugReport
                            )
                            
                            HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                            
                            SettingsActionRow(
                                title = "Export Logs",
                                description = "Save diagnostic logs to file",
                                icon = Icons.Default.FileDownload,
                                onClick = { /* Export logs */ }
                            )
                            
                            HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                            
                            SettingsActionRow(
                                title = "Network Diagnostics",
                                description = "Run network connectivity tests",
                                icon = Icons.Default.NetworkPing,
                                onClick = { /* Run diagnostics */ }
                            )
                        }
                    }
                }
            }
            
            // About Section
            item {
                SettingsSectionHeader("About", Icons.Default.Info)
            }
            
            item {
                SettingsCard {
                    Column {
                        SettingsActionRow(
                            title = "Version",
                            description = "1.0.0",
                            icon = Icons.Default.Info,
                            onClick = { }
                        )
                        
                        HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                        
                        SettingsActionRow(
                            title = "Privacy Policy",
                            description = "View privacy policy",
                            icon = Icons.Default.PrivacyTip,
                            onClick = { }
                        )
                        
                        HorizontalDivider(color = CardBackground, modifier = Modifier.padding(vertical = 8.dp))
                        
                        SettingsActionRow(
                            title = "Licenses",
                            description = "Open source licenses",
                            icon = Icons.Default.Description,
                            onClick = { }
                        )
                    }
                }
            }
        }
    }
    
    // Reset dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Preferences?") },
            text = { Text("This will reset all settings to their default values. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        // Reset settings
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ThreatRed)
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ElectricBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = ElectricBlue,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = VibrантGreen,
                checkedTrackColor = VibrантGreen.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun SettingsSelectionRow(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ElectricBlue
                )
            }
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextTertiary
        )
    }
}

@Composable
private fun SettingsActionRow(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    textColor: androidx.compose.ui.graphics.Color = TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

// Enums for settings
enum class FontSize(val label: String) {
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large")
}

enum class AppTheme(val label: String) {
    LIGHT("Light"),
    DARK("Dark"),
    AUTO("Auto")
}

enum class FirewallBehavior(val label: String) {
    ALLOW_ALL("Allow All"),
    BLOCK_ALL("Block All"),
    SMART("Smart Filter")
}
