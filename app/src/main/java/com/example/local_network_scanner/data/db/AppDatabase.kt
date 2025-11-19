package com.example.local_network_scanner.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        LogEntry::class,
        Profile::class,
        ProfileRule::class,
        AppUsageDaily::class,
        DailyStats::class,
        BlocklistEntry::class,
        UserProfile::class,
        SavedNetwork::class,
        NetworkPolicy::class,
        SpeedTestResult::class,
        NetworkAnalytics::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
    abstract fun profileDao(): ProfileDao
    abstract fun appUsageDao(): AppUsageDao
    abstract fun dailyStatsDao(): DailyStatsDao
    abstract fun blocklistDao(): BlocklistDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun savedNetworkDao(): SavedNetworkDao
    abstract fun networkPolicyDao(): NetworkPolicyDao
    abstract fun speedTestResultDao(): SpeedTestResultDao
    abstract fun networkAnalyticsDao(): NetworkAnalyticsDao
}
