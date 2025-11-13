package com.example.local_network_scanner.ui.viewmodel

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.model.AppNetworkActivity
import com.example.local_network_scanner.data.model.DataUsageStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ActivityViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    
    private val packageManager = application.packageManager
    
    private val _last5MinutesActivity = MutableStateFlow<List<AppNetworkActivity>>(emptyList())
    val last5MinutesActivity: StateFlow<List<AppNetworkActivity>> = _last5MinutesActivity
    
    private val _dataUsageStats = MutableStateFlow(DataUsageStats())
    val dataUsageStats: StateFlow<DataUsageStats> = _dataUsageStats
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    init {
        viewModelScope.launch {
            delay(500)
            _isLoading.value = false
            
            // Start monitoring
            while (isActive) {
                updateActivity()
                delay(1000) // Update every second
            }
        }
    }
    
    private suspend fun updateActivity() {
        withContext(Dispatchers.Default) {
            try {
                // Simulate network activity tracking
                // In production, this would read from VPN service or network stats
                val activities = generateMockActivities()
                
                var totalUpload = 0L
                var totalDownload = 0L
                
                activities.forEach { activity ->
                    totalUpload += activity.uploadBytes
                    totalDownload += activity.downloadBytes
                }
                
                _last5MinutesActivity.value = activities
                    .sortedByDescending { it.uploadBytes + it.downloadBytes }
                
                _dataUsageStats.value = DataUsageStats(
                    totalUpload = totalUpload,
                    totalDownload = totalDownload,
                    activeAppsCount = activities.size
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun generateMockActivities(): List<AppNetworkActivity> {
        val commonApps = listOf(
            "com.android.chrome",
            "com.whatsapp",
            "com.instagram.android",
            "com.spotify.music",
            "com.google.android.youtube",
            "com.twitter.android",
            "com.facebook.katana",
            "com.netflix.mediaclient"
        )
        
        return commonApps.mapNotNull { packageName ->
            try {
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                val appName = packageManager.getApplicationLabel(appInfo).toString()
                val appIcon = packageManager.getApplicationIcon(appInfo)
                
                AppNetworkActivity(
                    packageName = packageName,
                    appName = appName,
                    appIcon = appIcon,
                    connectionCount = Random.nextInt(1, 20),
                    uploadBytes = Random.nextLong(1000, 5000000),
                    downloadBytes = Random.nextLong(10000, 50000000)
                )
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }
    }
    
    fun refreshActivity() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(500)
            updateActivity()
            _isLoading.value = false
        }
    }
}
