package com.example.local_network_scanner.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.TrafficStats
import android.os.PowerManager
import com.example.local_network_scanner.data.model.NetworkSpeed
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    
    private val _networkSpeed = MutableStateFlow(NetworkSpeed())
    val networkSpeed: StateFlow<NetworkSpeed> = _networkSpeed
    
    private val _ping = MutableStateFlow(0)
    val ping: StateFlow<Int> = _ping
    
    private val _isMonitoring = MutableStateFlow(false)
    val isMonitoring: StateFlow<Boolean> = _isMonitoring
    
    private var lastRxBytes = 0L
    private var lastTxBytes = 0L
    private var lastTimestamp = 0L
    private var monitoringJob: Job? = null
    
    fun startMonitoring() {
        if (_isMonitoring.value) return
        
        _isMonitoring.value = true
        lastRxBytes = TrafficStats.getTotalRxBytes()
        lastTxBytes = TrafficStats.getTotalTxBytes()
        lastTimestamp = System.currentTimeMillis()
        
        monitoringJob = CoroutineScope(Dispatchers.Default).launch {
            launch {
                while (isActive && _isMonitoring.value) {
                    measureSpeed()
                    delay(getUpdateInterval())
                }
            }
            
            launch {
                while (isActive && _isMonitoring.value) {
                    measurePing()
                    delay(getUpdateInterval())
                }
            }
        }
    }
    
    fun stopMonitoring() {
        _isMonitoring.value = false
        monitoringJob?.cancel()
        monitoringJob = null
    }
    
    /**
     * Get update interval based on battery state
     * Returns longer intervals when battery is low to conserve power
     */
    private fun getUpdateInterval(): Long {
        return if (powerManager.isPowerSaveMode) {
            2000L // 2 seconds in power save mode
        } else {
            500L // 0.5 seconds in normal mode
        }
    }
    
    private fun measureSpeed() {
        try {
            val currentTime = System.currentTimeMillis()
            val currentRxBytes = TrafficStats.getTotalRxBytes()
            val currentTxBytes = TrafficStats.getTotalTxBytes()
            
            // Validate TrafficStats data
            if (currentRxBytes < 0 || currentTxBytes < 0) {
                // TrafficStats not supported on this device
                _networkSpeed.value = NetworkSpeed(0, 0)
                return
            }
            
            if (lastTimestamp > 0 && currentRxBytes >= lastRxBytes && currentTxBytes >= lastTxBytes) {
                val timeDiff = (currentTime - lastTimestamp) / 1000.0 // seconds
                
                if (timeDiff > 0) {
                    val downloadSpeed = ((currentRxBytes - lastRxBytes) / timeDiff).toLong()
                    val uploadSpeed = ((currentTxBytes - lastTxBytes) / timeDiff).toLong()
                    
                    _networkSpeed.value = NetworkSpeed(
                        downloadBytesPerSecond = downloadSpeed.coerceAtLeast(0),
                        uploadBytesPerSecond = uploadSpeed.coerceAtLeast(0)
                    )
                }
            }
            
            lastRxBytes = currentRxBytes
            lastTxBytes = currentTxBytes
            lastTimestamp = currentTime
        } catch (e: Exception) {
            e.printStackTrace()
            // Keep previous values on error
        }
    }
    
    private suspend fun measurePing() {
        withContext(Dispatchers.IO) {
            try {
                val startTime = System.currentTimeMillis()
                val socket = Socket()
                socket.connect(InetSocketAddress("8.8.8.8", 53), 3000)
                val endTime = System.currentTimeMillis()
                socket.close()
                
                _ping.value = (endTime - startTime).toInt()
            } catch (e: Exception) {
                _ping.value = -1 // Network unavailable
            }
        }
    }
}
