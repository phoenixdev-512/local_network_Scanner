package com.example.local_network_scanner.data.model

data class NetworkSpeed(
    val downloadBytesPerSecond: Long = 0,
    val uploadBytesPerSecond: Long = 0
) {
    val downloadMbps: Double get() = (downloadBytesPerSecond * 8.0) / (1024 * 1024)
    val uploadMbps: Double get() = (uploadBytesPerSecond * 8.0) / (1024 * 1024)
    val downloadKbps: Double get() = (downloadBytesPerSecond * 8.0) / 1024
    val uploadKbps: Double get() = (uploadBytesPerSecond * 8.0) / 1024
}
