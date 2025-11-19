package com.example.local_network_scanner.data.model

data class DataUsageStats(
    val total: Long = 0,
    val wifi: Long = 0,
    val mobile: Long = 0,
    val download: Long = 0,
    val upload: Long = 0,
    val apps: Long = 0
)

enum class TimeRange {
    TODAY,
    THIS_WEEK,
    THIS_MONTH,
    CUSTOM
}
