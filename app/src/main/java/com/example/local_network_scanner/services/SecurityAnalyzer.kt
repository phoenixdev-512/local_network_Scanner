package com.example.local_network_scanner.services

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.TrafficStats
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for analyzing device security and calculating security scores
 */
@Singleton
class SecurityAnalyzer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _securityScore = MutableStateFlow(0)
    val securityScore: StateFlow<Int> = _securityScore
    
    private val _threatsDetected = MutableStateFlow(0)
    val threatsDetected: StateFlow<Int> = _threatsDetected
    
    private val _activeConnections = MutableStateFlow(0)
    val activeConnections: StateFlow<Int> = _activeConnections
    
    private val _appsWithNetworkAccess = MutableStateFlow(0)
    val appsWithNetworkAccess: StateFlow<Int> = _appsWithNetworkAccess
    
    private val _suspiciousApps = MutableStateFlow<List<SuspiciousApp>>(emptyList())
    val suspiciousApps: StateFlow<List<SuspiciousApp>> = _suspiciousApps
    
    /**
     * Calculate comprehensive security score (0-100)
     * Based on: threat count, app permissions, network activity, blocked networks
     */
    suspend fun calculateSecurityScore(): Int = withContext(Dispatchers.Default) {
        var score = 100
        
        // Deduct points for threats detected
        val threats = _threatsDetected.value
        score -= (threats * 5).coerceAtMost(30) // Max -30 points
        
        // Deduct points for suspicious apps
        val suspiciousCount = _suspiciousApps.value.size
        score -= (suspiciousCount * 10).coerceAtMost(25) // Max -25 points
        
        // Deduct points for excessive network-accessing apps
        val appsCount = _appsWithNetworkAccess.value
        if (appsCount > 50) {
            score -= ((appsCount - 50) / 5).coerceAtMost(15) // Max -15 points
        }
        
        // Deduct points for unusual connection count
        val connections = _activeConnections.value
        if (connections > 100) {
            score -= ((connections - 100) / 10).coerceAtMost(15) // Max -15 points
        }
        
        val finalScore = score.coerceIn(0, 100)
        _securityScore.value = finalScore
        finalScore
    }
    
    /**
     * Scan installed apps for suspicious behavior
     */
    suspend fun scanForSuspiciousApps(): List<SuspiciousApp> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val suspicious = mutableListOf<SuspiciousApp>()
        
        for (app in installedApps) {
            // Skip system apps
            if (app.flags and ApplicationInfo.FLAG_SYSTEM != 0) continue
            
            val suspicionReasons = mutableListOf<String>()
            val packageName = app.packageName
            
            try {
                val packageInfo = packageManager.getPackageInfo(
                    packageName, 
                    PackageManager.GET_PERMISSIONS
                )
                
                val permissions = packageInfo.requestedPermissions ?: emptyArray()
                
                // Check for dangerous permission combinations
                val hasInternet = permissions.contains(android.Manifest.permission.INTERNET)
                val hasLocation = permissions.any { 
                    it.contains("LOCATION") || it == android.Manifest.permission.ACCESS_FINE_LOCATION 
                }
                val hasCamera = permissions.contains(android.Manifest.permission.CAMERA)
                val hasContacts = permissions.contains(android.Manifest.permission.READ_CONTACTS)
                val hasSms = permissions.any { it.contains("SMS") || it.contains("MMS") }
                val hasPhone = permissions.contains(android.Manifest.permission.READ_PHONE_STATE)
                
                // Flag apps with suspicious permission combinations
                if (hasInternet && hasLocation && hasCamera) {
                    suspicionReasons.add("Extensive permissions: Internet, Location, Camera")
                }
                
                if (hasInternet && hasSms) {
                    suspicionReasons.add("Can send data over internet with SMS access")
                }
                
                if (hasInternet && hasContacts && hasPhone) {
                    suspicionReasons.add("Can access contacts and phone state with internet")
                }
                
                // Check for excessive network usage
                val uid = app.uid
                val rxBytes = TrafficStats.getUidRxBytes(uid)
                val txBytes = TrafficStats.getUidTxBytes(uid)
                
                if (rxBytes > 100_000_000 || txBytes > 100_000_000) { // More than 100MB
                    suspicionReasons.add("High network usage detected")
                }
                
                if (suspicionReasons.isNotEmpty()) {
                    val appLabel = packageManager.getApplicationLabel(app).toString()
                    suspicious.add(
                        SuspiciousApp(
                            packageName = packageName,
                            appName = appLabel,
                            reasons = suspicionReasons,
                            riskLevel = calculateRiskLevel(suspicionReasons.size)
                        )
                    )
                }
            } catch (e: Exception) {
                // Skip apps that can't be analyzed
                continue
            }
        }
        
        _suspiciousApps.value = suspicious
        _threatsDetected.value = suspicious.count { it.riskLevel == RiskLevel.HIGH }
        suspicious
    }
    
    /**
     * Count apps with network access permission
     */
    suspend fun countAppsWithNetworkAccess(): Int = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        
        var count = 0
        for (app in installedApps) {
            try {
                val packageInfo = packageManager.getPackageInfo(
                    app.packageName, 
                    PackageManager.GET_PERMISSIONS
                )
                val permissions = packageInfo.requestedPermissions ?: emptyArray()
                if (permissions.contains(android.Manifest.permission.INTERNET)) {
                    count++
                }
            } catch (e: Exception) {
                continue
            }
        }
        
        _appsWithNetworkAccess.value = count
        count
    }
    
    /**
     * Update active connections count
     */
    fun updateActiveConnections(count: Int) {
        _activeConnections.value = count
    }
    
    /**
     * Update threats detected count
     */
    fun updateThreatsDetected(count: Int) {
        _threatsDetected.value = count
    }
    
    private fun calculateRiskLevel(reasonCount: Int): RiskLevel {
        return when {
            reasonCount >= 3 -> RiskLevel.HIGH
            reasonCount >= 2 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
    }
}

data class SuspiciousApp(
    val packageName: String,
    val appName: String,
    val reasons: List<String>,
    val riskLevel: RiskLevel
)

enum class RiskLevel {
    LOW, MEDIUM, HIGH
}
