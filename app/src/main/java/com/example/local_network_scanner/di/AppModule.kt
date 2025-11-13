package com.example.local_network_scanner.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.local_network_scanner.data.db.AppDatabase
import com.example.local_network_scanner.data.db.AppUsageDao
import com.example.local_network_scanner.data.db.BlocklistDao
import com.example.local_network_scanner.data.db.DailyStatsDao
import com.example.local_network_scanner.data.db.LogDao
import com.example.local_network_scanner.data.db.ProfileDao
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

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "netsentry_db"
        ).build()
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
}
