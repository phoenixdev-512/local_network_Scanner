package com.example.local_network_scanner.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.local_network_scanner.data.db.*
import com.example.local_network_scanner.data.repository.ProfileRepository
import com.example.local_network_scanner.util.ImageStorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    // Migration from version 5 to 6
    private val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create user_profiles table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS user_profiles (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL,
                    role TEXT NOT NULL,
                    avatarUri TEXT,
                    createdAt INTEGER NOT NULL,
                    autoStartVpn INTEGER NOT NULL DEFAULT 0,
                    notificationsEnabled INTEGER NOT NULL DEFAULT 1,
                    darkMode INTEGER NOT NULL DEFAULT 1,
                    selectedTheme TEXT NOT NULL DEFAULT 'default'
                )
            """.trimIndent())
            
            // Create saved_networks table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS saved_networks (
                    ssid TEXT PRIMARY KEY NOT NULL,
                    bssid TEXT NOT NULL,
                    securityType TEXT NOT NULL,
                    isTrusted INTEGER NOT NULL DEFAULT 0,
                    customDns TEXT,
                    firewallPolicyId INTEGER,
                    lastConnected INTEGER,
                    averageSpeed REAL NOT NULL DEFAULT 0.0,
                    signalStrength INTEGER NOT NULL DEFAULT 0
                )
            """.trimIndent())
            
            // Create network_policies table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS network_policies (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT NOT NULL,
                    allowedAppsJson TEXT NOT NULL DEFAULT '[]',
                    blockedDomainsJson TEXT NOT NULL DEFAULT '[]',
                    dnsProvider TEXT NOT NULL DEFAULT 'CLOUDFLARE',
                    enableAdBlocking INTEGER NOT NULL DEFAULT 0,
                    enableMalwareProtection INTEGER NOT NULL DEFAULT 1,
                    createdAt INTEGER NOT NULL
                )
            """.trimIndent())
            
            // Create speed_test_results table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS speed_test_results (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    ssid TEXT NOT NULL,
                    downloadSpeed REAL NOT NULL,
                    uploadSpeed REAL NOT NULL,
                    ping INTEGER NOT NULL,
                    jitter INTEGER NOT NULL DEFAULT 0,
                    timestamp INTEGER NOT NULL,
                    serverLocation TEXT
                )
            """.trimIndent())
        }
    }
    
    // Migration from version 6 to 7 - Add new profile fields
    private val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add new columns to user_profiles table
            database.execSQL("ALTER TABLE user_profiles ADD COLUMN lastActiveAt INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE user_profiles ADD COLUMN isActive INTEGER NOT NULL DEFAULT 0")
            database.execSQL("ALTER TABLE user_profiles ADD COLUMN customDnsServer TEXT")
            database.execSQL("ALTER TABLE user_profiles ADD COLUMN firewallRulesJson TEXT NOT NULL DEFAULT '[]'")
            database.execSQL("ALTER TABLE user_profiles ADD COLUMN blockedAppsJson TEXT NOT NULL DEFAULT '[]'")
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "netsentry_db"
        )
        .addMigrations(MIGRATION_5_6, MIGRATION_6_7)
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideLogDao(appDatabase: AppDatabase): LogDao {
        return appDatabase.logDao()
    }

    @Provides
    @Singleton
    fun provideProfileDao(appDatabase: AppDatabase): ProfileDao {
        return appDatabase.profileDao()
    }

    @Provides
    @Singleton
    fun provideAppUsageDao(appDatabase: AppDatabase): AppUsageDao {
        return appDatabase.appUsageDao()
    }

    @Provides
    @Singleton
    fun provideDailyStatsDao(appDatabase: AppDatabase): DailyStatsDao {
        return appDatabase.dailyStatsDao()
    }

    @Provides
    @Singleton
    fun provideBlocklistDao(appDatabase: AppDatabase): BlocklistDao {
        return appDatabase.blocklistDao()
    }
    
    @Provides
    @Singleton
    fun provideUserProfileDao(appDatabase: AppDatabase): UserProfileDao {
        return appDatabase.userProfileDao()
    }
    
    @Provides
    @Singleton
    fun provideSavedNetworkDao(appDatabase: AppDatabase): SavedNetworkDao {
        return appDatabase.savedNetworkDao()
    }
    
    @Provides
    @Singleton
    fun provideNetworkPolicyDao(appDatabase: AppDatabase): NetworkPolicyDao {
        return appDatabase.networkPolicyDao()
    }
    
    @Provides
    @Singleton
    fun provideSpeedTestResultDao(appDatabase: AppDatabase): SpeedTestResultDao {
        return appDatabase.speedTestResultDao()
    }
    
    @Provides
    @Singleton
    fun provideProfileRepository(userProfileDao: UserProfileDao): ProfileRepository {
        return ProfileRepository(userProfileDao)
    }
    
    @Provides
    @Singleton
    fun provideImageStorageService(@ApplicationContext context: Context): ImageStorageService {
        return ImageStorageService(context)
    }
}
