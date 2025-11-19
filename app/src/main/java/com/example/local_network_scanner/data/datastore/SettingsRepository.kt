package com.example.local_network_scanner.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "senet_settings")

@Singleton
class SettingsRepository @Inject constructor(@ApplicationContext private val context: Context) {

    // --- Appearance Settings Keys ---
    private val THEME = stringPreferencesKey("theme")
    private val ACCENT_COLOR = intPreferencesKey("accent_color")
    private val FONT_SIZE = floatPreferencesKey("font_size")
    private val CARD_STYLE = stringPreferencesKey("card_style")
    private val ICON_STYLE = stringPreferencesKey("icon_style")
    private val ANIMATION_INTENSITY = stringPreferencesKey("animation_intensity")

    // --- Network & VPN Settings Keys ---
    private val AUTO_START_VPN = booleanPreferencesKey("auto_start_vpn")
    private val AUTO_CONNECT_TRUSTED = booleanPreferencesKey("auto_connect_trusted")
    private val DEFAULT_DNS = stringPreferencesKey("default_dns")
    private val CUSTOM_DNS_PRIMARY = stringPreferencesKey("custom_dns_primary")
    private val CUSTOM_DNS_SECONDARY = stringPreferencesKey("custom_dns_secondary")
    private val VPN_PROTOCOL = stringPreferencesKey("vpn_protocol")
    private val KILL_SWITCH = booleanPreferencesKey("kill_switch")
    private val NETWORK_PREFERENCE = stringPreferencesKey("network_preference")

    // --- Security & Privacy Settings Keys ---
    private val SCAN_FREQUENCY = stringPreferencesKey("scan_frequency")
    private val THREAT_SENSITIVITY = stringPreferencesKey("threat_sensitivity")
    private val AUTO_BLOCK_THREATS = booleanPreferencesKey("auto_block_threats")
    private val SEND_USAGE_STATS = booleanPreferencesKey("send_usage_stats")
    private val CRASH_REPORTING = booleanPreferencesKey("crash_reporting")
    private val TELEMETRY = booleanPreferencesKey("telemetry")

    // --- Notification Settings Keys ---
    private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    private val THREAT_ALERTS = booleanPreferencesKey("threat_alerts")
    private val ALERT_PRIORITY = stringPreferencesKey("alert_priority")
    private val CONNECTION_LOGS = booleanPreferencesKey("connection_logs")
    private val SPEED_TEST_REMINDERS = booleanPreferencesKey("speed_test_reminders")
    private val WEEKLY_SUMMARY = booleanPreferencesKey("weekly_summary")
    private val NOTIFICATION_SOUND = stringPreferencesKey("notification_sound")
    private val VIBRATION = booleanPreferencesKey("vibration")
    private val VIBRATION_PATTERN = stringPreferencesKey("vibration_pattern")
    private val LED_COLOR = intPreferencesKey("led_color")

    // --- Data Usage Settings Keys ---
    private val DATA_SAVER = booleanPreferencesKey("data_saver")
    private val DATA_SAVER_UPDATE_FREQUENCY = stringPreferencesKey("data_saver_update_frequency")
    private val BACKGROUND_DATA_RESTRICTION = booleanPreferencesKey("background_data_restriction")
    private val WIFI_ONLY_SYNC = booleanPreferencesKey("wifi_only_sync")
    private val METERED_WARNING = booleanPreferencesKey("metered_warning")

    // --- Advanced Settings Keys ---
    private val NETWORK_SPEED_UNIT = stringPreferencesKey("network_speed_unit")
    private val DATA_USAGE_UNIT = stringPreferencesKey("data_usage_unit")
    private val DEBUG_LOGGING = booleanPreferencesKey("debug_logging")
    private val LOG_LEVEL = stringPreferencesKey("log_level")

    // --- Legacy Feature Keys (preserved for backwards compatibility) ---
    private val BYPASSED_APPS = stringSetPreferencesKey("bypassed_apps")
    private val DNS_MODE = stringPreferencesKey("dns_mode")
    private val CUSTOM_DNS_IP = stringPreferencesKey("custom_dns_ip")
    private val ENABLE_SECURE_DNS = booleanPreferencesKey("enable_secure_dns")
    private val ENABLE_WEEKLY_SUMMARY = booleanPreferencesKey("enable_weekly_summary")
    private val BLOCKED_COUNTRIES = stringSetPreferencesKey("blocked_countries_iso")
    private val PENDING_REVIEW_APPS = stringSetPreferencesKey("pending_review_apps")
    private val ENABLE_AD_BLOCKING = booleanPreferencesKey("enable_ad_blocking")
    private val ENABLE_MALWARE_BLOCKING = booleanPreferencesKey("enable_malware_blocking")
    private val NOTIFY_ON_THREATS = booleanPreferencesKey("notify_on_threats")
    private val NOTIFY_ON_NEW_APPS = booleanPreferencesKey("notify_on_new_apps")
    private val BLOCKLIST_LAST_UPDATED = stringPreferencesKey("blocklist_last_updated")

    // --- Appearance Settings Flows ---
    fun getTheme(): Flow<String> = context.dataStore.data.map { it[THEME] ?: "system" }
    suspend fun setTheme(theme: String) = context.dataStore.edit { it[THEME] = theme }

    fun getAccentColor(): Flow<Int> = context.dataStore.data.map { it[ACCENT_COLOR] ?: 0xFF1E88E5.toInt() }
    suspend fun setAccentColor(color: Int) = context.dataStore.edit { it[ACCENT_COLOR] = color }

    fun getFontSize(): Flow<Float> = context.dataStore.data.map { it[FONT_SIZE] ?: 1.0f }
    suspend fun setFontSize(size: Float) = context.dataStore.edit { it[FONT_SIZE] = size }

    fun getCardStyle(): Flow<String> = context.dataStore.data.map { it[CARD_STYLE] ?: "elevated" }
    suspend fun setCardStyle(style: String) = context.dataStore.edit { it[CARD_STYLE] = style }

    fun getIconStyle(): Flow<String> = context.dataStore.data.map { it[ICON_STYLE] ?: "filled" }
    suspend fun setIconStyle(style: String) = context.dataStore.edit { it[ICON_STYLE] = style }

    fun getAnimationIntensity(): Flow<String> = context.dataStore.data.map { it[ANIMATION_INTENSITY] ?: "medium" }
    suspend fun setAnimationIntensity(intensity: String) = context.dataStore.edit { it[ANIMATION_INTENSITY] = intensity }

    // --- Network & VPN Settings Flows ---
    fun getAutoStartVpn(): Flow<Boolean> = context.dataStore.data.map { it[AUTO_START_VPN] ?: false }
    suspend fun setAutoStartVpn(enabled: Boolean) = context.dataStore.edit { it[AUTO_START_VPN] = enabled }

    fun getAutoConnectTrusted(): Flow<Boolean> = context.dataStore.data.map { it[AUTO_CONNECT_TRUSTED] ?: false }
    suspend fun setAutoConnectTrusted(enabled: Boolean) = context.dataStore.edit { it[AUTO_CONNECT_TRUSTED] = enabled }

    fun getDefaultDns(): Flow<String> = context.dataStore.data.map { it[DEFAULT_DNS] ?: "Cloudflare" }
    suspend fun setDefaultDns(dns: String) = context.dataStore.edit { it[DEFAULT_DNS] = dns }

    fun getCustomDnsPrimary(): Flow<String> = context.dataStore.data.map { it[CUSTOM_DNS_PRIMARY] ?: "" }
    suspend fun setCustomDnsPrimary(ip: String) = context.dataStore.edit { it[CUSTOM_DNS_PRIMARY] = ip }

    fun getCustomDnsSecondary(): Flow<String> = context.dataStore.data.map { it[CUSTOM_DNS_SECONDARY] ?: "" }
    suspend fun setCustomDnsSecondary(ip: String) = context.dataStore.edit { it[CUSTOM_DNS_SECONDARY] = ip }

    fun getVpnProtocol(): Flow<String> = context.dataStore.data.map { it[VPN_PROTOCOL] ?: "OpenVPN" }
    suspend fun setVpnProtocol(protocol: String) = context.dataStore.edit { it[VPN_PROTOCOL] = protocol }

    fun getKillSwitch(): Flow<Boolean> = context.dataStore.data.map { it[KILL_SWITCH] ?: false }
    suspend fun setKillSwitch(enabled: Boolean) = context.dataStore.edit { it[KILL_SWITCH] = enabled }

    fun getNetworkPreference(): Flow<String> = context.dataStore.data.map { it[NETWORK_PREFERENCE] ?: "Auto" }
    suspend fun setNetworkPreference(preference: String) = context.dataStore.edit { it[NETWORK_PREFERENCE] = preference }

    // --- Security & Privacy Settings Flows ---
    fun getScanFrequency(): Flow<String> = context.dataStore.data.map { it[SCAN_FREQUENCY] ?: "On Connection Change" }
    suspend fun setScanFrequency(frequency: String) = context.dataStore.edit { it[SCAN_FREQUENCY] = frequency }

    fun getThreatSensitivity(): Flow<String> = context.dataStore.data.map { it[THREAT_SENSITIVITY] ?: "Medium" }
    suspend fun setThreatSensitivity(sensitivity: String) = context.dataStore.edit { it[THREAT_SENSITIVITY] = sensitivity }

    fun getAutoBlockThreats(): Flow<Boolean> = context.dataStore.data.map { it[AUTO_BLOCK_THREATS] ?: false }
    suspend fun setAutoBlockThreats(enabled: Boolean) = context.dataStore.edit { it[AUTO_BLOCK_THREATS] = enabled }

    fun getSendUsageStats(): Flow<Boolean> = context.dataStore.data.map { it[SEND_USAGE_STATS] ?: false }
    suspend fun setSendUsageStats(enabled: Boolean) = context.dataStore.edit { it[SEND_USAGE_STATS] = enabled }

    fun getCrashReporting(): Flow<Boolean> = context.dataStore.data.map { it[CRASH_REPORTING] ?: false }
    suspend fun setCrashReporting(enabled: Boolean) = context.dataStore.edit { it[CRASH_REPORTING] = enabled }

    fun getTelemetry(): Flow<Boolean> = context.dataStore.data.map { it[TELEMETRY] ?: false }
    suspend fun setTelemetry(enabled: Boolean) = context.dataStore.edit { it[TELEMETRY] = enabled }

    // --- Notification Settings Flows ---
    fun getNotificationsEnabled(): Flow<Boolean> = context.dataStore.data.map { it[NOTIFICATIONS_ENABLED] ?: true }
    suspend fun setNotificationsEnabled(enabled: Boolean) = context.dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled }

    fun getThreatAlerts(): Flow<Boolean> = context.dataStore.data.map { it[THREAT_ALERTS] ?: true }
    suspend fun setThreatAlerts(enabled: Boolean) = context.dataStore.edit { it[THREAT_ALERTS] = enabled }

    fun getAlertPriority(): Flow<String> = context.dataStore.data.map { it[ALERT_PRIORITY] ?: "Default" }
    suspend fun setAlertPriority(priority: String) = context.dataStore.edit { it[ALERT_PRIORITY] = priority }

    fun getConnectionLogs(): Flow<Boolean> = context.dataStore.data.map { it[CONNECTION_LOGS] ?: false }
    suspend fun setConnectionLogs(enabled: Boolean) = context.dataStore.edit { it[CONNECTION_LOGS] = enabled }

    fun getSpeedTestReminders(): Flow<Boolean> = context.dataStore.data.map { it[SPEED_TEST_REMINDERS] ?: false }
    suspend fun setSpeedTestReminders(enabled: Boolean) = context.dataStore.edit { it[SPEED_TEST_REMINDERS] = enabled }

    fun getWeeklySummary(): Flow<Boolean> = context.dataStore.data.map { it[WEEKLY_SUMMARY] ?: false }
    suspend fun setWeeklySummary(enabled: Boolean) = context.dataStore.edit { it[WEEKLY_SUMMARY] = enabled }

    fun getNotificationSound(): Flow<String> = context.dataStore.data.map { it[NOTIFICATION_SOUND] ?: "default" }
    suspend fun setNotificationSound(sound: String) = context.dataStore.edit { it[NOTIFICATION_SOUND] = sound }

    fun getVibration(): Flow<Boolean> = context.dataStore.data.map { it[VIBRATION] ?: true }
    suspend fun setVibration(enabled: Boolean) = context.dataStore.edit { it[VIBRATION] = enabled }

    fun getVibrationPattern(): Flow<String> = context.dataStore.data.map { it[VIBRATION_PATTERN] ?: "default" }
    suspend fun setVibrationPattern(pattern: String) = context.dataStore.edit { it[VIBRATION_PATTERN] = pattern }

    fun getLedColor(): Flow<Int> = context.dataStore.data.map { it[LED_COLOR] ?: 0xFF1E88E5.toInt() }
    suspend fun setLedColor(color: Int) = context.dataStore.edit { it[LED_COLOR] = color }

    // --- Data Usage Settings Flows ---
    fun getDataSaver(): Flow<Boolean> = context.dataStore.data.map { it[DATA_SAVER] ?: false }
    suspend fun setDataSaver(enabled: Boolean) = context.dataStore.edit { it[DATA_SAVER] = enabled }

    fun getDataSaverUpdateFrequency(): Flow<String> = context.dataStore.data.map { it[DATA_SAVER_UPDATE_FREQUENCY] ?: "Every 5s" }
    suspend fun setDataSaverUpdateFrequency(frequency: String) = context.dataStore.edit { it[DATA_SAVER_UPDATE_FREQUENCY] = frequency }

    fun getBackgroundDataRestriction(): Flow<Boolean> = context.dataStore.data.map { it[BACKGROUND_DATA_RESTRICTION] ?: false }
    suspend fun setBackgroundDataRestriction(enabled: Boolean) = context.dataStore.edit { it[BACKGROUND_DATA_RESTRICTION] = enabled }

    fun getWifiOnlySync(): Flow<Boolean> = context.dataStore.data.map { it[WIFI_ONLY_SYNC] ?: false }
    suspend fun setWifiOnlySync(enabled: Boolean) = context.dataStore.edit { it[WIFI_ONLY_SYNC] = enabled }

    fun getMeteredWarning(): Flow<Boolean> = context.dataStore.data.map { it[METERED_WARNING] ?: true }
    suspend fun setMeteredWarning(enabled: Boolean) = context.dataStore.edit { it[METERED_WARNING] = enabled }

    // --- Advanced Settings Flows ---
    fun getNetworkSpeedUnit(): Flow<String> = context.dataStore.data.map { it[NETWORK_SPEED_UNIT] ?: "Mbps" }
    suspend fun setNetworkSpeedUnit(unit: String) = context.dataStore.edit { it[NETWORK_SPEED_UNIT] = unit }

    fun getDataUsageUnit(): Flow<String> = context.dataStore.data.map { it[DATA_USAGE_UNIT] ?: "Auto" }
    suspend fun setDataUsageUnit(unit: String) = context.dataStore.edit { it[DATA_USAGE_UNIT] = unit }

    fun getDebugLogging(): Flow<Boolean> = context.dataStore.data.map { it[DEBUG_LOGGING] ?: false }
    suspend fun setDebugLogging(enabled: Boolean) = context.dataStore.edit { it[DEBUG_LOGGING] = enabled }

    fun getLogLevel(): Flow<String> = context.dataStore.data.map { it[LOG_LEVEL] ?: "Error" }
    suspend fun setLogLevel(level: String) = context.dataStore.edit { it[LOG_LEVEL] = level }

    // --- Utility Functions ---
    suspend fun resetToDefaults() {
        try {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
        } catch (e: Exception) {
            android.util.Log.e("SettingsRepository", "Error resetting to defaults", e)
            throw e
        }
    }

    suspend fun exportSettings(): Map<String, Any> {
        return try {
            val preferences = context.dataStore.data.first()
            preferences.asMap().mapKeys { it.key.name }
                .mapValues { entry -> entry.value ?: "" }
        } catch (e: Exception) {
            android.util.Log.e("SettingsRepository", "Error exporting settings", e)
            emptyMap()
        }
    }

    suspend fun importSettings(settings: Map<String, Any>) {
        try {
            context.dataStore.edit { preferences ->
                settings.forEach { (key, value) ->
                    when (value) {
                        is Boolean -> preferences[booleanPreferencesKey(key)] = value
                        is String -> preferences[stringPreferencesKey(key)] = value
                        is Int -> preferences[intPreferencesKey(key)] = value
                        is Float -> preferences[floatPreferencesKey(key)] = value
                        is Long -> preferences[intPreferencesKey(key)] = value.toInt()
                        is Double -> preferences[floatPreferencesKey(key)] = value.toFloat()
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("SettingsRepository", "Error importing settings", e)
            throw e
        }
    }

    // --- Legacy Functions (preserved for backwards compatibility) ---
    private fun getRuleKey(packageName: String) = booleanPreferencesKey(packageName)

    fun getRule(packageName: String): Flow<Boolean> {
        return context.dataStore.data.map {
            it[getRuleKey(packageName)] ?: true // Default to allowed
        }
    }

    suspend fun setRule(packageName: String, isAllowed: Boolean) {
        context.dataStore.edit {
            it[getRuleKey(packageName)] = isAllowed
        }
    }

    fun getBypassedApps(): Flow<Set<String>> {
        return context.dataStore.data.map { it[BYPASSED_APPS] ?: emptySet() }
    }

    suspend fun toggleBypassApp(packageName: String) {
        context.dataStore.edit {
            val currentBypassed = it[BYPASSED_APPS] ?: emptySet()
            val newBypassed = if (currentBypassed.contains(packageName)) {
                currentBypassed - packageName
            } else {
                currentBypassed + packageName
            }
            it[BYPASSED_APPS] = newBypassed
        }
    }

    fun blockAllByDefault(): Flow<Boolean> {
        return context.dataStore.data.map {
            it[booleanPreferencesKey("block_all_by_default")] ?: false
        }
    }

    suspend fun setBlockAllByDefault(block: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey("block_all_by_default")] = block
        }
    }

    fun getDnsSettings(): Flow<DnsSettings> {
        return context.dataStore.data.map {
            DnsSettings(
                dnsMode = it[DNS_MODE] ?: "DEFAULT",
                customDnsIp = it[CUSTOM_DNS_IP] ?: "",
                enableSecureDns = it[ENABLE_SECURE_DNS] ?: false
            )
        }
    }

    suspend fun setDnsMode(mode: String) {
        context.dataStore.edit { it[DNS_MODE] = mode }
    }

    suspend fun setCustomDnsIp(ip: String) {
        context.dataStore.edit { it[CUSTOM_DNS_IP] = ip }
    }

    suspend fun setEnableSecureDns(enable: Boolean) {
        context.dataStore.edit { it[ENABLE_SECURE_DNS] = enable }
    }

    fun getEnableWeeklySummary(): Flow<Boolean> {
        return context.dataStore.data.map { it[ENABLE_WEEKLY_SUMMARY] ?: false }
    }

    suspend fun setEnableWeeklySummary(enable: Boolean) {
        context.dataStore.edit { it[ENABLE_WEEKLY_SUMMARY] = enable }
    }

    fun getBlockedCountries(): Flow<Set<String>> {
        return context.dataStore.data.map { it[BLOCKED_COUNTRIES] ?: emptySet() }
    }

    suspend fun toggleBlockedCountry(isoCode: String) {
        context.dataStore.edit {
            val currentBlocked = it[BLOCKED_COUNTRIES] ?: emptySet()
            val newBlocked = if (currentBlocked.contains(isoCode)) {
                currentBlocked - isoCode
            } else {
                currentBlocked + isoCode
            }
            it[BLOCKED_COUNTRIES] = newBlocked
        }
    }
    
    fun getPendingReviewApps(): Flow<Set<String>> {
        return context.dataStore.data.map { it[PENDING_REVIEW_APPS] ?: emptySet() }
    }

    suspend fun addPendingReviewApp(packageName: String) {
        context.dataStore.edit {
            val current = it[PENDING_REVIEW_APPS] ?: emptySet()
            it[PENDING_REVIEW_APPS] = current + packageName
        }
    }

    suspend fun removePendingReviewApp(packageName: String) {
        context.dataStore.edit {
            val current = it[PENDING_REVIEW_APPS] ?: emptySet()
            it[PENDING_REVIEW_APPS] = current - packageName
        }
    }

    fun getAdBlockingEnabled(): Flow<Boolean> = context.dataStore.data.map { it[ENABLE_AD_BLOCKING] ?: false }
    suspend fun setAdBlockingEnabled(enabled: Boolean) = context.dataStore.edit { it[ENABLE_AD_BLOCKING] = enabled }

    fun getMalwareBlockingEnabled(): Flow<Boolean> = context.dataStore.data.map { it[ENABLE_MALWARE_BLOCKING] ?: false }
    suspend fun setMalwareBlockingEnabled(enabled: Boolean) = context.dataStore.edit { it[ENABLE_MALWARE_BLOCKING] = enabled }

    fun getNotifyOnThreats(): Flow<Boolean> = context.dataStore.data.map { it[NOTIFY_ON_THREATS] ?: false }
    suspend fun setNotifyOnThreats(enabled: Boolean) = context.dataStore.edit { it[NOTIFY_ON_THREATS] = enabled }

    fun getNotifyOnNewApps(): Flow<Boolean> = context.dataStore.data.map { it[NOTIFY_ON_NEW_APPS] ?: false }
    suspend fun setNotifyOnNewApps(enabled: Boolean) = context.dataStore.edit { it[NOTIFY_ON_NEW_APPS] = enabled }

    fun getBlocklistLastUpdated(): Flow<String> = context.dataStore.data.map { it[BLOCKLIST_LAST_UPDATED] ?: "Never" }
    suspend fun setBlocklistLastUpdated(timestamp: String) = context.dataStore.edit { it[BLOCKLIST_LAST_UPDATED] = timestamp }
}
