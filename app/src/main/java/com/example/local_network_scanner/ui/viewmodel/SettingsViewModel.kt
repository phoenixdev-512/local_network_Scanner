package com.example.local_network_scanner.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.datastore.SettingsRepository
import com.example.local_network_scanner.data.repository.ProfileRepository
import com.example.local_network_scanner.util.ConfigurationManager
import com.example.local_network_scanner.util.LogExporter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for Settings screen
 */
sealed class SettingsUiState {
    data object Idle : SettingsUiState()
    data object Loading : SettingsUiState()
    data class Success(val message: String) : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
    data object PickFile : SettingsUiState()
}

/**
 * Comprehensive ViewModel for Settings with full DataStore integration
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository,
    private val logExporter: LogExporter,
    private val configurationManager: ConfigurationManager
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Idle)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    // Dialog states
    private val _showResetDialog = MutableStateFlow(false)
    val showResetDialog: StateFlow<Boolean> = _showResetDialog.asStateFlow()

    private val _showClearDataDialog = MutableStateFlow(false)
    val showClearDataDialog: StateFlow<Boolean> = _showClearDataDialog.asStateFlow()

    private val _showCustomDnsDialog = MutableStateFlow(false)
    val showCustomDnsDialog: StateFlow<Boolean> = _showCustomDnsDialog.asStateFlow()

    private val _showColorPickerDialog = MutableStateFlow(false)
    val showColorPickerDialog: StateFlow<Boolean> = _showColorPickerDialog.asStateFlow()

    // --- Appearance Settings ---
    val theme = settingsRepository.getTheme()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "system")
    
    val accentColor = settingsRepository.getAccentColor()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0xFF1E88E5.toInt())
    
    val fontSize = settingsRepository.getFontSize()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.0f)
    
    val cardStyle = settingsRepository.getCardStyle()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "elevated")
    
    val iconStyle = settingsRepository.getIconStyle()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "filled")
    
    val animationIntensity = settingsRepository.getAnimationIntensity()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "medium")

    // --- Network & VPN Settings ---
    val autoStartVpn = settingsRepository.getAutoStartVpn()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val autoConnectTrusted = settingsRepository.getAutoConnectTrusted()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val defaultDns = settingsRepository.getDefaultDns()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Cloudflare")
    
    val customDnsPrimary = settingsRepository.getCustomDnsPrimary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    
    val customDnsSecondary = settingsRepository.getCustomDnsSecondary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    
    val vpnProtocol = settingsRepository.getVpnProtocol()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "OpenVPN")
    
    val killSwitch = settingsRepository.getKillSwitch()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val networkPreference = settingsRepository.getNetworkPreference()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Auto")

    // --- Security & Privacy Settings ---
    val scanFrequency = settingsRepository.getScanFrequency()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "On Connection Change")
    
    val threatSensitivity = settingsRepository.getThreatSensitivity()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Medium")
    
    val autoBlockThreats = settingsRepository.getAutoBlockThreats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val sendUsageStats = settingsRepository.getSendUsageStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val crashReporting = settingsRepository.getCrashReporting()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val telemetry = settingsRepository.getTelemetry()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // --- Notification Settings ---
    val notificationsEnabled = settingsRepository.getNotificationsEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    
    val threatAlerts = settingsRepository.getThreatAlerts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    
    val alertPriority = settingsRepository.getAlertPriority()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Default")
    
    val connectionLogs = settingsRepository.getConnectionLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val speedTestReminders = settingsRepository.getSpeedTestReminders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val weeklySummary = settingsRepository.getWeeklySummary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val notificationSound = settingsRepository.getNotificationSound()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "default")
    
    val vibration = settingsRepository.getVibration()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    
    val vibrationPattern = settingsRepository.getVibrationPattern()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "default")
    
    val ledColor = settingsRepository.getLedColor()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0xFF1E88E5.toInt())

    // --- Data Usage Settings ---
    val dataSaver = settingsRepository.getDataSaver()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val dataSaverUpdateFrequency = settingsRepository.getDataSaverUpdateFrequency()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Every 5s")
    
    val backgroundDataRestriction = settingsRepository.getBackgroundDataRestriction()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val wifiOnlySync = settingsRepository.getWifiOnlySync()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val meteredWarning = settingsRepository.getMeteredWarning()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    // --- Advanced Settings ---
    val networkSpeedUnit = settingsRepository.getNetworkSpeedUnit()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Mbps")
    
    val dataUsageUnit = settingsRepository.getDataUsageUnit()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Auto")
    
    val debugLogging = settingsRepository.getDebugLogging()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val logLevel = settingsRepository.getLogLevel()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Error")

    // Check if user is admin
    val isAdmin = profileRepository.activeProfile
        .map { profile -> profile?.role == com.example.local_network_scanner.data.db.UserRole.ADMIN }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // --- Setter Functions for Appearance ---
    fun setTheme(theme: String) = viewModelScope.launch {
        settingsRepository.setTheme(theme)
    }

    fun setAccentColor(color: Int) = viewModelScope.launch {
        settingsRepository.setAccentColor(color)
    }

    fun setFontSize(size: Float) = viewModelScope.launch {
        settingsRepository.setFontSize(size)
    }

    fun setCardStyle(style: String) = viewModelScope.launch {
        settingsRepository.setCardStyle(style)
    }

    fun setIconStyle(style: String) = viewModelScope.launch {
        settingsRepository.setIconStyle(style)
    }

    fun setAnimationIntensity(intensity: String) = viewModelScope.launch {
        settingsRepository.setAnimationIntensity(intensity)
    }

    // --- Setter Functions for Network & VPN ---
    fun setAutoStartVpn(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setAutoStartVpn(enabled)
    }

    fun setAutoConnectTrusted(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setAutoConnectTrusted(enabled)
    }

    fun setDefaultDns(dns: String) = viewModelScope.launch {
        settingsRepository.setDefaultDns(dns)
        if (dns == "Custom") {
            _showCustomDnsDialog.value = true
        }
    }

    fun setCustomDnsPrimary(ip: String) = viewModelScope.launch {
        settingsRepository.setCustomDnsPrimary(ip)
    }

    fun setCustomDnsSecondary(ip: String) = viewModelScope.launch {
        settingsRepository.setCustomDnsSecondary(ip)
    }

    fun setVpnProtocol(protocol: String) = viewModelScope.launch {
        settingsRepository.setVpnProtocol(protocol)
    }

    fun setKillSwitch(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setKillSwitch(enabled)
    }

    fun setNetworkPreference(preference: String) = viewModelScope.launch {
        settingsRepository.setNetworkPreference(preference)
    }

    // --- Setter Functions for Security & Privacy ---
    fun setScanFrequency(frequency: String) = viewModelScope.launch {
        settingsRepository.setScanFrequency(frequency)
    }

    fun setThreatSensitivity(sensitivity: String) = viewModelScope.launch {
        settingsRepository.setThreatSensitivity(sensitivity)
    }

    fun setAutoBlockThreats(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setAutoBlockThreats(enabled)
    }

    fun setSendUsageStats(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setSendUsageStats(enabled)
    }

    fun setCrashReporting(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setCrashReporting(enabled)
    }

    fun setTelemetry(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setTelemetry(enabled)
    }

    // --- Setter Functions for Notifications ---
    fun setNotificationsEnabled(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setNotificationsEnabled(enabled)
    }

    fun setThreatAlerts(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setThreatAlerts(enabled)
    }

    fun setAlertPriority(priority: String) = viewModelScope.launch {
        settingsRepository.setAlertPriority(priority)
    }

    fun setConnectionLogs(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setConnectionLogs(enabled)
    }

    fun setSpeedTestReminders(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setSpeedTestReminders(enabled)
    }

    fun setWeeklySummary(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setWeeklySummary(enabled)
    }

    fun setNotificationSound(sound: String) = viewModelScope.launch {
        settingsRepository.setNotificationSound(sound)
    }

    fun setVibration(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setVibration(enabled)
    }

    fun setVibrationPattern(pattern: String) = viewModelScope.launch {
        settingsRepository.setVibrationPattern(pattern)
    }

    fun setLedColor(color: Int) = viewModelScope.launch {
        settingsRepository.setLedColor(color)
    }

    // --- Setter Functions for Data Usage ---
    fun setDataSaver(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setDataSaver(enabled)
    }

    fun setDataSaverUpdateFrequency(frequency: String) = viewModelScope.launch {
        settingsRepository.setDataSaverUpdateFrequency(frequency)
    }

    fun setBackgroundDataRestriction(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setBackgroundDataRestriction(enabled)
    }

    fun setWifiOnlySync(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setWifiOnlySync(enabled)
    }

    fun setMeteredWarning(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setMeteredWarning(enabled)
    }

    // --- Setter Functions for Advanced ---
    fun setNetworkSpeedUnit(unit: String) = viewModelScope.launch {
        settingsRepository.setNetworkSpeedUnit(unit)
    }

    fun setDataUsageUnit(unit: String) = viewModelScope.launch {
        settingsRepository.setDataUsageUnit(unit)
    }

    fun setDebugLogging(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setDebugLogging(enabled)
    }

    fun setLogLevel(level: String) = viewModelScope.launch {
        settingsRepository.setLogLevel(level)
    }

    // --- Advanced Operations ---
    fun exportLogs() = viewModelScope.launch {
        _uiState.value = SettingsUiState.Loading
        try {
            val result = logExporter.exportLogs()
            result.onSuccess { file ->
                _uiState.value = SettingsUiState.Success("Logs exported to: ${file.name}")
                logExporter.clearOldLogs()
            }.onFailure { error ->
                _uiState.value = SettingsUiState.Error("Failed to export logs: ${error.message}")
            }
        } catch (e: Exception) {
            _uiState.value = SettingsUiState.Error("Error: ${e.message}")
        }
    }

    fun exportConfiguration() = viewModelScope.launch {
        _uiState.value = SettingsUiState.Loading
        try {
            val settings = settingsRepository.exportSettings()
            val result = configurationManager.exportToFile(settings)
            result.onSuccess { file ->
                _uiState.value = SettingsUiState.Success("Configuration exported to: ${file.name}")
                configurationManager.clearOldConfigs()
            }.onFailure { error ->
                _uiState.value = SettingsUiState.Error("Failed to export configuration: ${error.message}")
            }
        } catch (e: Exception) {
            _uiState.value = SettingsUiState.Error("Error: ${e.message}")
        }
    }

    fun importConfiguration() {
        _uiState.value = SettingsUiState.PickFile
    }

    fun importConfigurationFromUri(uri: Uri) = viewModelScope.launch {
        _uiState.value = SettingsUiState.Loading
        try {
            val result = configurationManager.importFromUri(uri)
            result.onSuccess { settings ->
                if (configurationManager.validateSettings(settings)) {
                    settingsRepository.importSettings(settings)
                    _uiState.value = SettingsUiState.Success("Configuration imported successfully")
                } else {
                    _uiState.value = SettingsUiState.Error("Invalid configuration file format")
                }
            }.onFailure { error ->
                _uiState.value = SettingsUiState.Error("Failed to import: ${error.message}")
            }
        } catch (e: Exception) {
            _uiState.value = SettingsUiState.Error("Error: ${e.message}")
        }
    }

    // --- Dialog Functions ---
    fun showResetDialog() {
        _showResetDialog.value = true
    }

    fun confirmReset() = viewModelScope.launch {
        _showResetDialog.value = false
        _uiState.value = SettingsUiState.Loading
        try {
            settingsRepository.resetToDefaults()
            _uiState.value = SettingsUiState.Success("Settings reset to defaults")
        } catch (e: Exception) {
            _uiState.value = SettingsUiState.Error("Failed to reset settings: ${e.message}")
        }
    }

    fun cancelReset() {
        _showResetDialog.value = false
    }

    fun showClearDataDialog() {
        _showClearDataDialog.value = true
    }

    fun confirmClearData() = viewModelScope.launch {
        _showClearDataDialog.value = false
        _uiState.value = SettingsUiState.Loading
        try {
            // Clear all settings
            settingsRepository.resetToDefaults()
            // TODO: Clear database and other app data as needed
            _uiState.value = SettingsUiState.Success("All data cleared")
        } catch (e: Exception) {
            _uiState.value = SettingsUiState.Error("Failed to clear data: ${e.message}")
        }
    }

    fun cancelClearData() {
        _showClearDataDialog.value = false
    }

    fun dismissCustomDnsDialog() {
        _showCustomDnsDialog.value = false
    }

    fun showColorPicker() {
        _showColorPickerDialog.value = true
    }

    fun dismissColorPicker() {
        _showColorPickerDialog.value = false
    }

    // --- External Actions ---
    fun checkForUpdates() {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open Play Store", Toast.LENGTH_SHORT).show()
        }
    }

    fun openPrivacyPolicy() {
        openUrl("https://github.com/phoenixdev-512/local_network_Scanner/blob/main/PRIVACY_POLICY.md")
    }

    fun openTermsOfService() {
        openUrl("https://github.com/phoenixdev-512/local_network_Scanner/blob/main/TERMS_OF_SERVICE.md")
    }

    fun contactDeveloper() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:developer@senet.app")
                putExtra(Intent.EXTRA_SUBJECT, "SENET App Support")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show()
        }
    }

    fun openGitHub() {
        openUrl("https://github.com/phoenixdev-512/local_network_Scanner")
    }

    fun rateOnPlayStore() {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open Play Store", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open URL", Toast.LENGTH_SHORT).show()
        }
    }

    fun dismissUiState() {
        _uiState.value = SettingsUiState.Idle
    }
}
