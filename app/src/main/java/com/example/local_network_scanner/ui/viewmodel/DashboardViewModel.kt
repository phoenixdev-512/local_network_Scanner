package com.example.local_network_scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.services.NetworkMonitor
import com.example.local_network_scanner.ui.NetworkStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Dashboard screen
 * Manages network monitoring state and statistics
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    
    private val _isMonitoring = MutableStateFlow(false)
    val isMonitoring: StateFlow<Boolean> = _isMonitoring.asStateFlow()
    
    private val _networkStats = MutableStateFlow(NetworkStats())
    val networkStats: StateFlow<NetworkStats> = _networkStats.asStateFlow()
    
    // Real-time network metrics
    val networkSpeed = networkMonitor.networkSpeed
    val ping = networkMonitor.ping
    
    init {
        // Initialize with default or mock data
        loadNetworkStats()
        
        // Start live monitoring
        networkMonitor.startMonitoring()
    }
    
    private fun loadNetworkStats() {
        viewModelScope.launch {
            // TODO: Load actual network statistics from repositories
            _networkStats.value = NetworkStats(
                downloadSpeed = 45.2,
                uploadSpeed = 12.3,
                ping = 18,
                threatsBlocked = 127,
                activeConnections = 8,
                securityScore = 85,
                dataUsedMB = 342.5f,
                dataTotalMB = 1024f,
                connectedDevices = 5
            )
        }
    }
    
    fun startMonitoring() {
        _isMonitoring.value = true
        networkMonitor.startMonitoring()
    }
    
    fun stopMonitoring() {
        _isMonitoring.value = false
        networkMonitor.stopMonitoring()
    }
    
    fun refreshStats() {
        loadNetworkStats()
    }
    
    fun triggerWiFiScan() {
        // Trigger WiFi scan action
        viewModelScope.launch {
            // TODO: Implement actual WiFi scan trigger
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        networkMonitor.stopMonitoring()
    }
}
