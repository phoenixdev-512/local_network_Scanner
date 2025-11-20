package com.example.local_network_scanner.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import androidx.core.content.ContextCompat
import com.example.local_network_scanner.data.db.SavedNetwork
import com.example.local_network_scanner.data.db.SavedNetworkDao
import com.example.local_network_scanner.data.db.SecurityType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for network operations
 */
@Singleton
class NetworkRepository @Inject constructor(
    private val networkDao: SavedNetworkDao,
    private val wifiManager: WifiManager,
    @ApplicationContext private val context: Context
) {
    val allNetworks: Flow<List<SavedNetwork>> = networkDao.getAllNetworks()
    val trustedNetworks: Flow<List<SavedNetwork>> = networkDao.getTrustedNetworks()
    
    suspend fun saveNetwork(network: SavedNetwork): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            networkDao.insertNetwork(network)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateNetwork(network: SavedNetwork): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            networkDao.updateNetwork(network)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteNetwork(ssid: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            networkDao.deleteNetworkBySsid(ssid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getNetworkBySsid(ssid: String): SavedNetwork? = withContext(Dispatchers.IO) {
        networkDao.getNetworkBySsid(ssid)
    }
    
    suspend fun scanNetworks(): List<WifiNetwork> = withContext(Dispatchers.IO) {
        if (!hasLocationPermission()) {
            throw SecurityException("Location permission required")
        }
        
        val scanResults = wifiManager.scanResults
        val currentSsid = getCurrentConnectedSsid()
        
        scanResults.map { result ->
            WifiNetwork(
                ssid = result.SSID,
                bssid = result.BSSID,
                signalStrength = WifiManager.calculateSignalLevel(result.level, 100),
                securityType = determineSecurityType(result.capabilities),
                isConnected = result.SSID == currentSsid,
                isSaved = networkDao.getNetworkBySsid(result.SSID) != null
            )
        }
    }
    
    suspend fun connectToNetwork(ssid: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            // Note: Connecting to WiFi programmatically is restricted in Android 10+
            // This is a simplified implementation
            Result.success(false)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun getCurrentConnectedSsid(): String? {
        val wifiInfo = wifiManager.connectionInfo
        return wifiInfo?.ssid?.removeSurrounding("\"")
    }
    
    private fun determineSecurityType(capabilities: String): SecurityType {
        return when {
            capabilities.contains("WPA3") -> SecurityType.WPA3
            capabilities.contains("WPA2") -> SecurityType.WPA2
            capabilities.contains("WPA") -> SecurityType.WPA
            capabilities.contains("WEP") -> SecurityType.WEP
            else -> SecurityType.OPEN
        }
    }
}

data class WifiNetwork(
    val ssid: String,
    val bssid: String,
    val signalStrength: Int,
    val securityType: SecurityType,
    val isConnected: Boolean = false,
    val isSaved: Boolean = false
)
