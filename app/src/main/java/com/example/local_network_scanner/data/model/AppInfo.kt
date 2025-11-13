package com.example.local_network_scanner.data.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val appName: String,
    val packageName: String,
    val icon: Drawable,
    var isBlocked: Boolean,
    val isBypassed: Boolean,
    val dataUsage: Long
)
