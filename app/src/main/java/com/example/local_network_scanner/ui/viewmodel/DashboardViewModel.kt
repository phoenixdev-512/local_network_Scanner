package com.example.local_network_scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    // TODO: Inject repositories when implementing data layer
) : ViewModel() {
    
    private val _isMonitoring = MutableStateFlow(false)
    val isMonitoring: StateFlow<Boolean> = _isMonitoring.asStateFlow()
    
    private val _networkStats = MutableStateFlow(NetworkStats())
    val networkStats: StateFlow<NetworkStats> = _networkStats.asStateFlow()
    
    init {
        // Initialize with default or mock data
        loadNetworkStats()
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
        // TODO: Start actual network monitoring
    }
    
    fun stopMonitoring() {
        _isMonitoring.value = false
        // TODO: Stop network monitoring
    }
    
    fun refreshStats() {
        loadNetworkStats()
    }
}
