package com.example.local_network_scanner.data.repository

import com.example.local_network_scanner.data.db.NetworkAnalytics
import com.example.local_network_scanner.data.db.NetworkAnalyticsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for network analytics operations
 */
@Singleton
class AnalyticsRepository @Inject constructor(
    private val analyticsDao: NetworkAnalyticsDao
) {
    fun getAnalytics(ssid: String, timeRange: TimeRange): Flow<List<NetworkAnalytics>> {
        val endTime = System.currentTimeMillis()
        val startTime = when (timeRange) {
            TimeRange.HOURS_24 -> endTime - (24 * 60 * 60 * 1000)
            TimeRange.DAYS_7 -> endTime - (7 * 24 * 60 * 60 * 1000)
            TimeRange.DAYS_30 -> endTime - (30 * 24 * 60 * 60 * 1000)
        }
        return analyticsDao.getAnalytics(ssid, startTime, endTime)
    }
    
    suspend fun recordAnalytics(analytics: NetworkAnalytics) = withContext(Dispatchers.IO) {
        analyticsDao.insertAnalytics(analytics)
    }
    
    suspend fun cleanOldAnalytics() = withContext(Dispatchers.IO) {
        val cutoffTime = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L)
        analyticsDao.deleteOldAnalytics(cutoffTime)
    }
}

enum class TimeRange(val label: String) {
    HOURS_24("24 Hours"),
    DAYS_7("7 Days"),
    DAYS_30("30 Days")
}
