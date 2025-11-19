package com.example.local_network_scanner.services

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for monitoring network data usage using NetworkStatsManager API
 * Provides accurate real-time data usage statistics for mobile and WiFi
 */
@Singleton
class DataUsageMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val networkStatsManager = context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
    
    /**
     * Get total data usage statistics for a given time range
     */
    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getDataUsageStats(timeRange: TimeRange): DataUsageStats = withContext(Dispatchers.IO) {
        val endTime = System.currentTimeMillis()
        val startTime = when (timeRange) {
            TimeRange.TODAY -> getStartOfDay()
            TimeRange.WEEK -> endTime - (7 * 24 * 60 * 60 * 1000)
            TimeRange.MONTH -> endTime - (30 * 24 * 60 * 60 * 1000)
        }
        
        // Mobile data usage
        val mobileStats = try {
            networkStatsManager.querySummaryForDevice(
                ConnectivityManager.TYPE_MOBILE,
                null,
                startTime,
                endTime
            )
        } catch (e: Exception) {
            null
        }
        
        // WiFi data usage
        val wifiStats = try {
            networkStatsManager.querySummaryForDevice(
                ConnectivityManager.TYPE_WIFI,
                null,
                startTime,
                endTime
            )
        } catch (e: Exception) {
            null
        }
        
        DataUsageStats(
            totalRx = (mobileStats?.rxBytes ?: 0) + (wifiStats?.rxBytes ?: 0),
            totalTx = (mobileStats?.txBytes ?: 0) + (wifiStats?.txBytes ?: 0),
            mobileRx = mobileStats?.rxBytes ?: 0,
            mobileTx = mobileStats?.txBytes ?: 0,
            wifiRx = wifiStats?.rxBytes ?: 0,
            wifiTx = wifiStats?.txBytes ?: 0
        )
    }
    
    /**
     * Get per-app data usage statistics for a given time range
     */
    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getPerAppDataUsage(timeRange: TimeRange): List<AppDataUsage> = withContext(Dispatchers.IO) {
        val endTime = System.currentTimeMillis()
        val startTime = getStartTimeForRange(timeRange)
        
        val networkStatsBucket = try {
            networkStatsManager.querySummary(
                ConnectivityManager.TYPE_WIFI,
                null,
                startTime,
                endTime
            )
        } catch (e: Exception) {
            return@withContext emptyList()
        }
        
        val appUsageMap = mutableMapOf<Int, AppDataUsage>()
        
        while (networkStatsBucket.hasNextBucket()) {
            val bucket = NetworkStats.Bucket()
            networkStatsBucket.getNextBucket(bucket)
            
            val uid = bucket.uid
            val rxBytes = bucket.rxBytes
            val txBytes = bucket.txBytes
            
            if (uid > 0) { // Filter out system UIDs
                val existing = appUsageMap[uid]
                if (existing != null) {
                    appUsageMap[uid] = existing.copy(
                        downloadBytes = existing.downloadBytes + rxBytes,
                        uploadBytes = existing.uploadBytes + txBytes
                    )
                } else {
                    val packageName = getPackageNameFromUid(uid)
                    if (packageName != null) {
                        appUsageMap[uid] = AppDataUsage(
                            uid = uid,
                            packageName = packageName,
                            appName = getAppName(packageName),
                            downloadBytes = rxBytes,
                            uploadBytes = txBytes
                        )
                    }
                }
            }
        }
        
        appUsageMap.values.sortedByDescending { it.total }
    }
    
    /**
     * Get package name from UID
     */
    private fun getPackageNameFromUid(uid: Int): String? {
        return try {
            context.packageManager.getPackagesForUid(uid)?.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Get human-readable app name from package name
     */
    private fun getAppName(packageName: String): String {
        return try {
            val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
            context.packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            packageName
        }
    }
    
    /**
     * Get the start of the current day in milliseconds
     */
    private fun getStartOfDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    /**
     * Get start time for a given time range
     */
    private fun getStartTimeForRange(timeRange: TimeRange): Long {
        val endTime = System.currentTimeMillis()
        return when (timeRange) {
            TimeRange.TODAY -> getStartOfDay()
            TimeRange.WEEK -> endTime - (7 * 24 * 60 * 60 * 1000)
            TimeRange.MONTH -> endTime - (30 * 24 * 60 * 60 * 1000)
        }
    }
}

/**
 * Data class representing overall data usage statistics
 */
data class DataUsageStats(
    val totalRx: Long,
    val totalTx: Long,
    val mobileRx: Long,
    val mobileTx: Long,
    val wifiRx: Long,
    val wifiTx: Long
) {
    val total: Long get() = totalRx + totalTx
    val mobile: Long get() = mobileRx + mobileTx
    val wifi: Long get() = wifiRx + wifiTx
}

/**
 * Data class representing per-app data usage
 */
data class AppDataUsage(
    val uid: Int,
    val packageName: String,
    val appName: String,
    val downloadBytes: Long,
    val uploadBytes: Long
) {
    val total: Long get() = downloadBytes + uploadBytes
}

/**
 * Enum representing different time ranges for data usage queries
 */
enum class TimeRange {
    TODAY, WEEK, MONTH
}
