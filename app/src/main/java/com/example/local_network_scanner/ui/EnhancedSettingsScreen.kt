package com.example.local_network_scanner.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.local_network_scanner.ui.theme.*
import com.example.local_network_scanner.ui.viewmodel.SettingsUiState
import com.example.local_network_scanner.ui.viewmodel.SettingsViewModel
import com.example.local_network_scanner.util.isValidIp

/**
 * Comprehensive Enhanced Settings Screen with full DataStore integration
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedSettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    // Collect all state
    val theme by viewModel.theme.collectAsState()
    val accentColor by viewModel.accentColor.collectAsState()
    val fontSize by viewModel.fontSize.collectAsState()
    val cardStyle by viewModel.cardStyle.collectAsState()
    val iconStyle by viewModel.iconStyle.collectAsState()
    val animationIntensity by viewModel.animationIntensity.collectAsState()
    
    val autoStartVpn by viewModel.autoStartVpn.collectAsState()
    val autoConnectTrusted by viewModel.autoConnectTrusted.collectAsState()
    val defaultDns by viewModel.defaultDns.collectAsState()
    val customDnsPrimary by viewModel.customDnsPrimary.collectAsState()
    val customDnsSecondary by viewModel.customDnsSecondary.collectAsState()
    val vpnProtocol by viewModel.vpnProtocol.collectAsState()
    val killSwitch by viewModel.killSwitch.collectAsState()
    val networkPreference by viewModel.networkPreference.collectAsState()
    
    val scanFrequency by viewModel.scanFrequency.collectAsState()
    val threatSensitivity by viewModel.threatSensitivity.collectAsState()
    val autoBlockThreats by viewModel.autoBlockThreats.collectAsState()
    val sendUsageStats by viewModel.sendUsageStats.collectAsState()
    val crashReporting by viewModel.crashReporting.collectAsState()
    val telemetry by viewModel.telemetry.collectAsState()
    
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val threatAlerts by viewModel.threatAlerts.collectAsState()
    val alertPriority by viewModel.alertPriority.collectAsState()
    val connectionLogs by viewModel.connectionLogs.collectAsState()
    val speedTestReminders by viewModel.speedTestReminders.collectAsState()
    val weeklySummary by viewModel.weeklySummary.collectAsState()
    val notificationSound by viewModel.notificationSound.collectAsState()
    val vibration by viewModel.vibration.collectAsState()
    val vibrationPattern by viewModel.vibrationPattern.collectAsState()
    val ledColor by viewModel.ledColor.collectAsState()
    
    val dataSaver by viewModel.dataSaver.collectAsState()
    val dataSaverUpdateFrequency by viewModel.dataSaverUpdateFrequency.collectAsState()
    val backgroundDataRestriction by viewModel.backgroundDataRestriction.collectAsState()
    val wifiOnlySync by viewModel.wifiOnlySync.collectAsState()
    val meteredWarning by viewModel.meteredWarning.collectAsState()
    
    val networkSpeedUnit by viewModel.networkSpeedUnit.collectAsState()
    val dataUsageUnit by viewModel.dataUsageUnit.collectAsState()
    val debugLogging by viewModel.debugLogging.collectAsState()
    val logLevel by viewModel.logLevel.collectAsState()
    
    val isAdmin by viewModel.isAdmin.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val showResetDialog by viewModel.showResetDialog.collectAsState()
    val showClearDataDialog by viewModel.showClearDataDialog.collectAsState()
    val showCustomDnsDialog by viewModel.showCustomDnsDialog.collectAsState()
    val showColorPickerDialog by viewModel.showColorPickerDialog.collectAsState()

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.importConfigurationFromUri(it) }
    }

    // Launch file picker when state changes
    LaunchedEffect(uiState) {
        if (uiState is SettingsUiState.PickFile) {
            filePickerLauncher.launch("application/json")
            viewModel.dismissUiState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    containerColor = Color.Transparent
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
                // Section 1: Appearance
                item {
                    SettingsSectionCard(title = "Appearance", icon = Icons.Filled.Palette) {
                        DropdownSettingItem(
                            title = "Theme",
                            subtitle = "App color theme",
                            currentValue = theme,
                            options = listOf("system", "light", "dark", "amoled"),
                            onValueChange = { viewModel.setTheme(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        ColorPickerSettingItem(
                            title = "Accent Color",
                            subtitle = "Primary theme color",
                            currentColor = accentColor,
                            onClick = { viewModel.showColorPicker() }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SliderSettingItem(
                            title = "Font Size",
                            subtitle = "Adjust text size",
                            value = fontSize,
                            valueRange = 0.8f..1.5f,
                            onValueChange = { viewModel.setFontSize(it) },
                            valueLabel = { "${(it * 100).toInt()}%" }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        DropdownSettingItem(
                            title = "Card Style",
                            subtitle = "UI card appearance",
                            currentValue = cardStyle,
                            options = listOf("elevated", "filled", "outlined"),
                            onValueChange = { viewModel.setCardStyle(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        DropdownSettingItem(
                            title = "Icon Style",
                            subtitle = "Icon appearance",
                            currentValue = iconStyle,
                            options = listOf("filled", "outlined"),
                            onValueChange = { viewModel.setIconStyle(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        DropdownSettingItem(
                            title = "Animation Intensity",
                            subtitle = "UI animation level",
                            currentValue = animationIntensity,
                            options = listOf("off", "low", "medium", "high"),
                            onValueChange = { viewModel.setAnimationIntensity(it) }
                        )
                    }
                }
                
                // Section 2: Network & VPN
                item {
                    SettingsSectionCard(title = "Network & VPN", icon = Icons.Filled.Wifi) {
                        SwitchSettingItem(
                            title = "Auto-start VPN",
                            subtitle = "Start VPN when device boots",
                            checked = autoStartVpn,
                            onCheckedChange = { viewModel.setAutoStartVpn(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Auto-connect Trusted Networks",
                            subtitle = "Connect to known safe networks",
                            checked = autoConnectTrusted,
                            onCheckedChange = { viewModel.setAutoConnectTrusted(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        DropdownSettingItem(
                            title = "Default DNS Provider",
                            subtitle = "DNS server selection",
                            currentValue = defaultDns,
                            options = listOf("Cloudflare", "Google", "Quad9", "Custom"),
                            onValueChange = { viewModel.setDefaultDns(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        DropdownSettingItem(
                            title = "VPN Protocol",
                            subtitle = "Connection protocol",
                            currentValue = vpnProtocol,
                            options = listOf("OpenVPN", "WireGuard", "IKEv2"),
                            onValueChange = { viewModel.setVpnProtocol(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Kill Switch",
                            subtitle = "Block internet if VPN disconnects",
                            checked = killSwitch,
                            onCheckedChange = { viewModel.setKillSwitch(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        DropdownSettingItem(
                            title = "Network Preference",
                            subtitle = "Preferred connection type",
                            currentValue = networkPreference,
                            options = listOf("WiFi Only", "Mobile Data Only", "Auto"),
                            onValueChange = { viewModel.setNetworkPreference(it) }
                        )
                    }
                }
                
                // Section 3: Security & Privacy
                item {
                    SettingsSectionCard(title = "Security & Privacy", icon = Icons.Filled.Security) {
                        DropdownSettingItem(
                            title = "Auto-scan Frequency",
                            subtitle = "Security scan schedule",
                            currentValue = scanFrequency,
                            options = listOf("Never", "Daily", "Weekly", "On Connection Change"),
                            onValueChange = { viewModel.setScanFrequency(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        DropdownSettingItem(
                            title = "Threat Sensitivity",
                            subtitle = "Detection sensitivity level",
                            currentValue = threatSensitivity,
                            options = listOf("Low", "Medium", "High"),
                            onValueChange = { viewModel.setThreatSensitivity(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Automatic Threat Blocking",
                            subtitle = "Block detected threats automatically",
                            checked = autoBlockThreats,
                            onCheckedChange = { viewModel.setAutoBlockThreats(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Send Anonymous Usage Statistics",
                            subtitle = "Help improve the app",
                            checked = sendUsageStats,
                            onCheckedChange = { viewModel.setSendUsageStats(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Crash Reporting",
                            subtitle = "Send crash reports to developers",
                            checked = crashReporting,
                            onCheckedChange = { viewModel.setCrashReporting(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Telemetry",
                            subtitle = "Share app usage data",
                            checked = telemetry,
                            onCheckedChange = { viewModel.setTelemetry(it) }
                        )
                    }
                }
                
                // Section 4: Notifications
                item {
                    SettingsSectionCard(title = "Notifications", icon = Icons.Filled.Notifications) {
                        SwitchSettingItem(
                            title = "Enable Notifications",
                            subtitle = "Master notification toggle",
                            checked = notificationsEnabled,
                            onCheckedChange = { viewModel.setNotificationsEnabled(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Threat Alerts",
                            subtitle = "Get notified of security threats",
                            checked = threatAlerts,
                            enabled = notificationsEnabled,
                            onCheckedChange = { viewModel.setThreatAlerts(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        DropdownSettingItem(
                            title = "Alert Priority",
                            subtitle = "Notification importance level",
                            currentValue = alertPriority,
                            options = listOf("Low", "Default", "High", "Urgent"),
                            enabled = notificationsEnabled,
                            onValueChange = { viewModel.setAlertPriority(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Connection Logs",
                            subtitle = "Notify on new connections",
                            checked = connectionLogs,
                            enabled = notificationsEnabled,
                            onCheckedChange = { viewModel.setConnectionLogs(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Speed Test Reminders",
                            subtitle = "Regular speed test prompts",
                            checked = speedTestReminders,
                            enabled = notificationsEnabled,
                            onCheckedChange = { viewModel.setSpeedTestReminders(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Weekly Summary",
                            subtitle = "Receive weekly usage reports",
                            checked = weeklySummary,
                            enabled = notificationsEnabled,
                            onCheckedChange = { viewModel.setWeeklySummary(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Vibration",
                            subtitle = "Vibrate on notifications",
                            checked = vibration,
                            enabled = notificationsEnabled,
                            onCheckedChange = { viewModel.setVibration(it) }
                        )
                    }
                }
                
                // Section 5: Data Usage
                item {
                    SettingsSectionCard(title = "Data Usage", icon = Icons.Filled.DataUsage) {
                        SwitchSettingItem(
                            title = "Data Saver Mode",
                            subtitle = "Reduce data consumption",
                            checked = dataSaver,
                            onCheckedChange = { viewModel.setDataSaver(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        DropdownSettingItem(
                            title = "Update Frequency",
                            subtitle = "Data refresh interval",
                            currentValue = dataSaverUpdateFrequency,
                            options = listOf("Every 2s", "Every 5s", "Every 10s", "Every 30s"),
                            enabled = dataSaver,
                            onValueChange = { viewModel.setDataSaverUpdateFrequency(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Background Data Restriction",
                            subtitle = "Limit background data usage",
                            checked = backgroundDataRestriction,
                            onCheckedChange = { viewModel.setBackgroundDataRestriction(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "WiFi Only Sync",
                            subtitle = "Sync only on WiFi",
                            checked = wifiOnlySync,
                            onCheckedChange = { viewModel.setWifiOnlySync(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        SwitchSettingItem(
                            title = "Metered Connection Warning",
                            subtitle = "Warn on metered networks",
                            checked = meteredWarning,
                            onCheckedChange = { viewModel.setMeteredWarning(it) }
                        )
                    }
                }
                
                // Section 6: Advanced
                item {
                    SettingsSectionCard(title = "Advanced", icon = Icons.Filled.Settings) {
                        DropdownSettingItem(
                            title = "Network Speed Unit",
                            subtitle = "Speed display format",
                            currentValue = networkSpeedUnit,
                            options = listOf("Mbps", "MB/s", "Kbps", "KB/s"),
                            onValueChange = { viewModel.setNetworkSpeedUnit(it) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        DropdownSettingItem(
                            title = "Data Usage Unit",
                            subtitle = "Usage display format",
                            currentValue = dataUsageUnit,
                            options = listOf("Auto", "KB", "MB", "GB"),
                            onValueChange = { viewModel.setDataUsageUnit(it) }
                        )
                        
                        if (isAdmin) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                            
                            SwitchSettingItem(
                                title = "Debug Logging",
                                subtitle = "Enable detailed logs",
                                checked = debugLogging,
                                onCheckedChange = { viewModel.setDebugLogging(it) }
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                            
                            DropdownSettingItem(
                                title = "Log Level",
                                subtitle = "Logging verbosity",
                                currentValue = logLevel,
                                options = listOf("Error", "Warning", "Info", "Debug", "Verbose"),
                                enabled = debugLogging,
                                onValueChange = { viewModel.setLogLevel(it) }
                            )
                        }
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        ClickableSettingItem(
                            title = "Export Logs",
                            subtitle = "Save app logs to file",
                            onClick = { viewModel.exportLogs() }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        ClickableSettingItem(
                            title = "Export Configuration",
                            subtitle = "Backup settings as JSON",
                            onClick = { viewModel.exportConfiguration() }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        ClickableSettingItem(
                            title = "Import Configuration",
                            subtitle = "Restore from backup",
                            onClick = { viewModel.importConfiguration() }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        ClickableSettingItem(
                            title = "Clear All Data",
                            subtitle = "Delete all app data",
                            textColor = WarningOrange,
                            onClick = { viewModel.showClearDataDialog() }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        ClickableSettingItem(
                            title = "Reset to Defaults",
                            subtitle = "Restore default settings",
                            textColor = WarningOrange,
                            onClick = { viewModel.showResetDialog() }
                        )
                    }
                }
                
                // Section 7: About & Legal
                item {
                    SettingsSectionCard(title = "About & Legal", icon = Icons.Filled.Info) {
                        InfoSettingItem(
                            title = "Version",
                            value = "1.0.0"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        ClickableSettingItem(
                            title = "Check for Updates",
                            subtitle = "Visit Google Play Store",
                            onClick = { viewModel.checkForUpdates() }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        ClickableSettingItem(
                            title = "Privacy Policy",
                            subtitle = "Read our privacy policy",
                            onClick = { viewModel.openPrivacyPolicy() }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        ClickableSettingItem(
                            title = "Terms of Service",
                            subtitle = "Read our terms",
                            onClick = { viewModel.openTermsOfService() }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        ClickableSettingItem(
                            title = "Contact Developer",
                            subtitle = "Send email to developer",
                            onClick = { viewModel.contactDeveloper() }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        ClickableSettingItem(
                            title = "GitHub Repository",
                            subtitle = "View source code",
                            onClick = { viewModel.openGitHub() }
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = CardBackground)
                        
                        ClickableSettingItem(
                            title = "Rate on Play Store",
                            subtitle = "Leave a review",
                            onClick = { viewModel.rateOnPlayStore() }
                        )
                    }
                }
            }
        }

        // Loading Overlay
        if (uiState is SettingsUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ElectricBlue)
            }
        }
    }

    // Dialogs
    if (showResetDialog) {
        ResetConfirmationDialog(
            onConfirm = { viewModel.confirmReset() },
            onDismiss = { viewModel.cancelReset() }
        )
    }

    if (showClearDataDialog) {
        ClearDataConfirmationDialog(
            onConfirm = { viewModel.confirmClearData() },
            onDismiss = { viewModel.cancelClearData() }
        )
    }

    if (showCustomDnsDialog) {
        CustomDnsDialog(
            primaryDns = customDnsPrimary,
            secondaryDns = customDnsSecondary,
            onSave = { primary, secondary ->
                viewModel.setCustomDnsPrimary(primary)
                viewModel.setCustomDnsSecondary(secondary)
                viewModel.dismissCustomDnsDialog()
            },
            onDismiss = { viewModel.dismissCustomDnsDialog() }
        )
    }

    if (showColorPickerDialog) {
        ColorPickerDialog(
            currentColor = accentColor,
            onColorSelected = { color ->
                viewModel.setAccentColor(color)
                viewModel.dismissColorPicker()
            },
            onDismiss = { viewModel.dismissColorPicker() }
        )
    }

    // Show success/error messages
    LaunchedEffect(uiState) {
        when (uiState) {
            is SettingsUiState.Success -> {
                // Could show a Snackbar here
                viewModel.dismissUiState()
            }
            is SettingsUiState.Error -> {
                // Could show error Snackbar here
                viewModel.dismissUiState()
            }
            else -> {}
        }
    }
}

// ============================================================================
// UI COMPONENTS
// ============================================================================

@Composable
fun SettingsSectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
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
            
            content()
        }
    }
}

@Composable
fun SwitchSettingItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onCheckedChange(!checked) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) TextPrimary else TextTertiary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = if (enabled) TextTertiary else TextTertiary.copy(alpha = 0.5f)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = VibrантGreen,
                checkedTrackColor = VibrантGreen.copy(alpha = 0.5f),
                uncheckedThumbColor = TextTertiary,
                uncheckedTrackColor = CardBackground,
                disabledCheckedThumbColor = VibrантGreen.copy(alpha = 0.5f),
                disabledUncheckedThumbColor = TextTertiary.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun DropdownSettingItem(
    title: String,
    subtitle: String,
    currentValue: String,
    options: List<String>,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { expanded = !expanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (enabled) TextPrimary else TextTertiary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (enabled) TextTertiary else TextTertiary.copy(alpha = 0.5f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = currentValue,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (enabled) ElectricBlue else ElectricBlue.copy(alpha = 0.5f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    tint = if (enabled) TextSecondary else TextSecondary.copy(alpha = 0.5f)
                )
            }
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(SurfaceDarkGray)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = if (option == currentValue) ElectricBlue else TextPrimary
                        )
                    },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SliderSettingItem(
    title: String,
    subtitle: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    valueLabel: (Float) -> String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
            }
            Text(
                text = valueLabel(value),
                style = MaterialTheme.typography.bodyMedium,
                color = ElectricBlue
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = ElectricBlue,
                activeTrackColor = ElectricBlue,
                inactiveTrackColor = CardBackground
            )
        )
    }
}

@Composable
fun ClickableSettingItem(
    title: String,
    subtitle: String,
    textColor: Color = TextPrimary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary
            )
        }
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = TextTertiary
        )
    }
}

@Composable
fun InfoSettingItem(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

@Composable
fun ColorPickerSettingItem(
    title: String,
    subtitle: String,
    currentColor: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary
            )
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(currentColor))
                .border(2.dp, TextSecondary, CircleShape)
        )
    }
}

// ============================================================================
// DIALOGS
// ============================================================================

@Composable
fun ColorPickerDialog(
    currentColor: Int,
    onColorSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = listOf(
        0xFF1E88E5.toInt() to "Blue",
        0xFFE53935.toInt() to "Red",
        0xFF43A047.toInt() to "Green",
        0xFFFB8C00.toInt() to "Orange",
        0xFF8E24AA.toInt() to "Purple",
        0xFFFDD835.toInt() to "Yellow",
        0xFF00ACC1.toInt() to "Cyan",
        0xFFD81B60.toInt() to "Pink"
    )
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Select Accent Color",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    colors.chunked(4).forEach { rowColors ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            rowColors.forEach { (color, name) ->
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(Color(color))
                                        .border(
                                            width = if (color == currentColor) 3.dp else 0.dp,
                                            color = if (color == currentColor) Color.White else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { onColorSelected(color) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (color == currentColor) {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CardBackground)
                ) {
                    Text("Cancel", color = TextPrimary)
                }
            }
        }
    }
}

@Composable
fun CustomDnsDialog(
    primaryDns: String,
    secondaryDns: String,
    onSave: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var primary by remember { mutableStateOf(primaryDns) }
    var secondary by remember { mutableStateOf(secondaryDns) }
    val isPrimaryValid = isValidIp(primary)
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDarkGray)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Custom DNS Servers",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                
                OutlinedTextField(
                    value = primary,
                    onValueChange = { primary = it },
                    label = { Text("Primary DNS *", color = TextSecondary) },
                    placeholder = { Text("1.1.1.1", color = TextTertiary) },
                    isError = primary.isNotEmpty() && !isPrimaryValid,
                    supportingText = {
                        if (primary.isNotEmpty() && !isPrimaryValid) {
                            Text("Invalid IP address", color = WarningOrange)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = TextSecondary
                    )
                )
                
                OutlinedTextField(
                    value = secondary,
                    onValueChange = { secondary = it },
                    label = { Text("Secondary DNS (Optional)", color = TextSecondary) },
                    placeholder = { Text("1.0.0.1", color = TextTertiary) },
                    isError = secondary.isNotEmpty() && !isValidIp(secondary),
                    supportingText = {
                        if (secondary.isNotEmpty() && !isValidIp(secondary)) {
                            Text("Invalid IP address", color = WarningOrange)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = TextSecondary
                    )
                )
                
                Text(
                    text = "Popular DNS Providers:\n• Cloudflare: 1.1.1.1 / 1.0.0.1\n• Google: 8.8.8.8 / 8.8.4.4\n• Quad9: 9.9.9.9 / 149.112.112.112",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CardBackground)
                    ) {
                        Text("Cancel", color = TextPrimary)
                    }
                    Button(
                        onClick = { onSave(primary, secondary) },
                        modifier = Modifier.weight(1f),
                        enabled = isPrimaryValid && (secondary.isEmpty() || isValidIp(secondary)),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                    ) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ResetConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDarkGray,
        title = {
            Text(
                text = "Reset Settings?",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "This will restore all settings to their default values. This action cannot be undone.",
                color = TextSecondary
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = WarningOrange)
            ) {
                Text("Reset", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = CardBackground)
            ) {
                Text("Cancel", color = TextPrimary)
            }
        }
    )
}

@Composable
fun ClearDataConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDarkGray,
        icon = {
            Icon(
                Icons.Filled.Warning,
                contentDescription = null,
                tint = WarningOrange,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = "Clear All Data?",
                color = WarningOrange,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "This will delete ALL app data including profiles, logs, and settings. This action cannot be undone.",
                color = TextSecondary
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = WarningOrange)
            ) {
                Text("Clear Data", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = CardBackground)
            ) {
                Text("Cancel", color = TextPrimary)
            }
        }
    )
}
