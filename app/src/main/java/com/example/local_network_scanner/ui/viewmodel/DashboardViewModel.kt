package com.example.local_network_scanner.ui.viewmodel

import android.net.TrafficStats
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.datastore.SettingsRepository
import com.example.local_network_scanner.data.model.DataUsageStats
import com.example.local_network_scanner.data.model.SpeedUnit
import com.example.local_network_scanner.data.model.TimeRange
import com.example.local_network_scanner.services.DataUsageMonitor
import com.example.local_network_scanner.services.DeviceScanner
import com.example.local_network_scanner.services.NetworkMonitor
import com.example.local_network_scanner.services.SecurityAnalyzer
import com.example.local_network_scanner.ui.NetworkStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Dashboard screen
 * Manages network monitoring state and statistics with real-time updates
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    private val securityAnalyzer: SecurityAnalyzer,
    private val deviceScanner: DeviceScanner,
    private val dataUsageMonitor: DataUsageMonitor,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _isMonitoring = MutableStateFlow(false)
    val isMonitoring: StateFlow<Boolean> = _isMonitoring.asStateFlow()
    
    private val _networkStats = MutableStateFlow(NetworkStats())
    val networkStats: StateFlow<NetworkStats> = _networkStats.asStateFlow()
    
    // Real-time network metrics from NetworkMonitor
    val networkSpeed = networkMonitor.networkSpeed
    val ping = networkMonitor.ping
    
    // Security metrics from SecurityAnalyzer
    val securityScore = securityAnalyzer.securityScore
    val threatsDetected = securityAnalyzer.threatsDetected
    val appsWithNetworkAccess = securityAnalyzer.appsWithNetworkAccess
    val activeConnections = securityAnalyzer.activeConnections
    
    // Security scanning state
    private val _isSecurityScanning = MutableStateFlow(false)
    val isSecurityScanning: StateFlow<Boolean> = _isSecurityScanning
    
    private val _lastSecurityScanTime = MutableStateFlow(0L)
    val lastSecurityScanTime: StateFlow<Long> = _lastSecurityScanTime
    
    // Speed unit from settings
    val speedUnit = settingsRepository.getNetworkSpeedUnit().map { unitString ->
        SpeedUnit.values().find { it.label == unitString } ?: SpeedUnit.MBPS
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SpeedUnit.MBPS)
    
    // Data usage
    private val _dataUsageStats = MutableStateFlow(DataUsageStats())
    val dataUsageStats: StateFlow<DataUsageStats> = _dataUsageStats
    
    private val _selectedTimeRange = MutableStateFlow(TimeRange.TODAY)
    val selectedTimeRange: StateFlow<TimeRange> = _selectedTimeRange
    
    // Device scanner
    val connectedDevicesCount = deviceScanner.connectedDevicesCount
    
    init {
        // Start live monitoring
        networkMonitor.startMonitoring()
        _isMonitoring.value = true
        
        // Initialize security analysis
        viewModelScope.launch {
            securityAnalyzer.countAppsWithNetworkAccess()
            securityAnalyzer.calculateSecurityScore()
        }
        
        // Load initial data
        loadInitialData()
        
        // Start periodic updates for dashboard stats
        startPeriodicUpdates()
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            // Load last security scan time
            _lastSecurityScanTime.value = getLastScanTime()
            
            // Load initial data usage
            updateDataUsage()
        }
    }
    
    private fun startPeriodicUpdates() {
        // Update network stats every 2 seconds
        viewModelScope.launch {
            while (isActive && _isMonitoring.value) {
                updateNetworkStats()
                delay(2000)
            }
        }
        
        // Update security score every 5 seconds
        viewModelScope.launch {
            while (isActive && _isMonitoring.value) {
                securityAnalyzer.calculateSecurityScore()
                delay(5000)
            }
        }
        
        // Update connected devices count every 10 seconds
        viewModelScope.launch {
            while (isActive && _isMonitoring.value) {
                deviceScanner.estimateDeviceCount()
                delay(10000)
            }
        }
        
        // Update data usage every minute
        viewModelScope.launch {
            while (isActive && _isMonitoring.value) {
                updateDataUsage()
                delay(60_000)
            }
        }
    }
    
    private suspend fun updateNetworkStats() {
        try {
            // Calculate data usage from TrafficStats
            val totalRxBytes = TrafficStats.getTotalRxBytes()
            val totalTxBytes = TrafficStats.getTotalTxBytes()
            
            // Convert to MB
            val dataUsedMB = ((totalRxBytes + totalTxBytes) / (1024f * 1024f))
            
            // Estimate active connections (simplified)
            val estimatedConnections = if (networkSpeed.value.downloadBytesPerSecond > 0 || 
                                           networkSpeed.value.uploadBytesPerSecond > 0) {
                (5..15).random() // Active data transfer suggests multiple connections
            } else {
                (0..3).random() // Idle state
            }
            
            securityAnalyzer.updateActiveConnections(estimatedConnections)
            
            _networkStats.value = NetworkStats(
                downloadSpeed = networkSpeed.value.downloadMbps,
                uploadSpeed = networkSpeed.value.uploadMbps,
                ping = ping.value,
                threatsBlocked = threatsDetected.value,
                activeConnections = estimatedConnections,
                securityScore = securityScore.value,
                dataUsedMB = dataUsedMB.coerceAtMost(10000f), // Cap at 10GB for display
                dataTotalMB = 10000f, // 10GB limit for visualization
                connectedDevices = connectedDevicesCount.value
            )
        } catch (e: Exception) {
            // Keep previous stats on error
        }
    }
    
    fun startMonitoring() {
        _isMonitoring.value = true
        networkMonitor.startMonitoring()
        startPeriodicUpdates()
    }
    
    fun stopMonitoring() {
        _isMonitoring.value = false
        networkMonitor.stopMonitoring()
    }
    
    fun refreshStats() {
        viewModelScope.launch {
            updateNetworkStats()
            securityAnalyzer.calculateSecurityScore()
            deviceScanner.estimateDeviceCount()
        }
    }
    
    fun triggerWiFiScan() {
        viewModelScope.launch {
            deviceScanner.scanNetwork()
        }
    }
    
    fun startSecurityScan() {
        viewModelScope.launch {
            _isSecurityScanning.value = true
            try {
                // Perform deep security scan
                val result = securityAnalyzer.performDeepScan()
                
                // Update security score (already updated in SecurityAnalyzer)
                
                // Save scan time
                _lastSecurityScanTime.value = System.currentTimeMillis()
                saveLastScanTime(_lastSecurityScanTime.value)
                
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Security scan failed", e)
            } finally {
                _isSecurityScanning.value = false
            }
        }
    }
    
    fun toggleSpeedUnit() {
        viewModelScope.launch {
            val currentUnit = speedUnit.value
            val nextUnit = when (currentUnit) {
                SpeedUnit.MBPS -> SpeedUnit.MBS
                SpeedUnit.MBS -> SpeedUnit.KBPS
                SpeedUnit.KBPS -> SpeedUnit.KBS
                SpeedUnit.KBS -> SpeedUnit.MBPS
            }
            settingsRepository.setNetworkSpeedUnit(nextUnit.label)
        }
    }
    
    fun setTimeRange(range: TimeRange) {
        _selectedTimeRange.value = range
        viewModelScope.launch {
            updateDataUsage()
        }
    }
    
    private suspend fun updateDataUsage() {
        try {
            val stats = dataUsageMonitor.getDataUsageStats(_selectedTimeRange.value)
            _dataUsageStats.value = stats
        } catch (e: Exception) {
            Log.e("DashboardViewModel", "Failed to update data usage", e)
        }
    }
    
    private suspend fun getLastScanTime(): Long {
        // Load from DataStore or SharedPreferences
        // For now, return 0 (never scanned)
        return 0L
    }
    
    private suspend fun saveLastScanTime(time: Long) {
        // Save to DataStore or SharedPreferences
        // For now, just log
        Log.d("DashboardViewModel", "Security scan completed at $time")
    }
    
    override fun onCleared() {
        super.onCleared()
        networkMonitor.stopMonitoring()
    }
}
