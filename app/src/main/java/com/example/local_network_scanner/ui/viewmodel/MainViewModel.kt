package com.example.local_network_scanner.ui.viewmodel

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.db.Profile
import com.example.local_network_scanner.data.db.ProfileDao
import com.example.local_network_scanner.vpn.NetSentryVpnService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileDao: ProfileDao
) : ViewModel() {

    val profiles = profileDao.getAllProfiles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeProfile = profileDao.getActiveProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isVpnActive = MutableStateFlow(false)
    val isVpnActive: StateFlow<Boolean> = _isVpnActive
    
    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress
    
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    init {
        // Check initial VPN state
        viewModelScope.launch {
            while (isActive) {
                _isVpnActive.value = isVpnServiceRunning()
                delay(1000)
            }
        }
    }

    fun startVpn() {
        viewModelScope.launch {
            try {
                val serviceIntent = Intent(context, NetSentryVpnService::class.java).apply {
                    action = "START"
                }
                context.startService(serviceIntent)
                
                delay(500)
                _isVpnActive.value = isVpnServiceRunning()
                
                // Start network scanning
                startNetworkScan()
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error starting VPN", e)
            }
        }
    }

    fun stopVpn() {
        viewModelScope.launch {
            try {
                val serviceIntent = Intent(context, NetSentryVpnService::class.java).apply {
                    action = "STOP"
                }
                context.startService(serviceIntent)
                delay(500)
                _isVpnActive.value = false
                _isScanning.value = false
                _scanProgress.value = 0f
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error stopping VPN", e)
            }
        }
    }
    
    private suspend fun startNetworkScan() {
        _isScanning.value = true
        _scanProgress.value = 0f
        
        // Simulate scanning progress
        for (i in 0..100 step 5) {
            if (!_isVpnActive.value) break
            _scanProgress.value = i / 100f
            delay(100)
        }
        
        _isScanning.value = false
    }
    
    private fun isVpnServiceRunning(): Boolean {
        return try {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            manager.getRunningServices(Integer.MAX_VALUE)
                .any { it.service.className == NetSentryVpnService::class.java.name }
        } catch (e: Exception) {
            false
        }
    }

    fun setActiveProfile(profile: Profile) {
        viewModelScope.launch {
            profileDao.switchActiveProfile(profile.id)
        }
    }

    fun addProfile(name: String) {
        viewModelScope.launch {
            profileDao.saveProfile(Profile(name = name))
        }
    }
}