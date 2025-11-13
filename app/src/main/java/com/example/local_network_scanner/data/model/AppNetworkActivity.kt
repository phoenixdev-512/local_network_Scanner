package com.example.local_network_scanner.data.model

import android.graphics.drawable.Drawable

data class AppNetworkActivity(
    val packageName: String,
    val appName: String,
    val appIcon: Drawable? = null,
    val connectionCount: Int = 0,
    val uploadBytes: Long = 0,
    val downloadBytes: Long = 0,
    val lastActiveTimestamp: Long = System.currentTimeMillis()
)

data class DataUsageStats(
    val totalUpload: Long = 0,
    val totalDownload: Long = 0,
    val activeAppsCount: Int = 0
)
