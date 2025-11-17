package com.example.local_network_scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.services.SecurityAnalyzer
import com.example.local_network_scanner.services.SuspiciousApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Security/Firewall screen
 * Handles deep security scans and threat detection
 */
@HiltViewModel
class SecurityViewModel @Inject constructor(
    private val securityAnalyzer: SecurityAnalyzer
) : ViewModel() {
    
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()
    
    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()
    
    private val _suspiciousApps = MutableStateFlow<List<SuspiciousApp>>(emptyList())
    val suspiciousApps: StateFlow<List<SuspiciousApp>> = _suspiciousApps.asStateFlow()
    
    private val _scanComplete = MutableStateFlow(false)
    val scanComplete: StateFlow<Boolean> = _scanComplete.asStateFlow()
    
    // Security metrics
    val securityScore = securityAnalyzer.securityScore
    val threatsDetected = securityAnalyzer.threatsDetected
    val appsWithNetworkAccess = securityAnalyzer.appsWithNetworkAccess
    val activeConnections = securityAnalyzer.activeConnections
    
    init {
        // Load initial security metrics
        viewModelScope.launch {
            securityAnalyzer.countAppsWithNetworkAccess()
            securityAnalyzer.calculateSecurityScore()
        }
    }
    
    /**
     * Perform deep security scan of the device
     */
    fun performDeepScan() {
        viewModelScope.launch {
            _isScanning.value = true
            _scanProgress.value = 0f
            _scanComplete.value = false
            
            // Simulate scanning progress with actual work
            try {
                // Phase 1: Count apps with network access (20%)
                _scanProgress.value = 0.1f
                delay(300)
                securityAnalyzer.countAppsWithNetworkAccess()
                _scanProgress.value = 0.2f
                delay(300)
                
                // Phase 2: Scan for suspicious apps (60%)
                _scanProgress.value = 0.3f
                delay(300)
                val suspicious = securityAnalyzer.scanForSuspiciousApps()
                _suspiciousApps.value = suspicious
                
                // Simulate progress during intensive scan
                for (i in 30..80 step 10) {
                    _scanProgress.value = i / 100f
                    delay(200)
                }
                
                // Phase 3: Calculate security score (20%)
                _scanProgress.value = 0.85f
                delay(300)
                securityAnalyzer.calculateSecurityScore()
                _scanProgress.value = 0.95f
                delay(200)
                
                // Complete
                _scanProgress.value = 1.0f
                delay(300)
                _scanComplete.value = true
            } catch (e: Exception) {
                // Handle scan errors
                _scanProgress.value = 0f
            } finally {
                _isScanning.value = false
            }
        }
    }
    
    /**
     * Reset scan state
     */
    fun resetScan() {
        _scanComplete.value = false
        _scanProgress.value = 0f
        _suspiciousApps.value = emptyList()
    }
}
