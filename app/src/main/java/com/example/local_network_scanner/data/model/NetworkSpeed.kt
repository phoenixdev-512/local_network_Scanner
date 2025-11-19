package com.example.local_network_scanner.data.model

data class NetworkSpeed(
    val downloadBytesPerSecond: Long = 0,
    val uploadBytesPerSecond: Long = 0
) {
    val downloadMbps: Double get() = (downloadBytesPerSecond * 8.0) / (1024 * 1024)
    val uploadMbps: Double get() = (uploadBytesPerSecond * 8.0) / (1024 * 1024)
    val downloadKbps: Double get() = (downloadBytesPerSecond * 8.0) / 1024
    val uploadKbps: Double get() = (uploadBytesPerSecond * 8.0) / 1024
    
    // Convert to various units
    fun getDownloadSpeed(unit: SpeedUnit): Double {
        return when (unit) {
            SpeedUnit.MBPS -> (downloadBytesPerSecond * 8.0) / (1024 * 1024)
            SpeedUnit.MBS -> downloadBytesPerSecond.toDouble() / (1024 * 1024)
            SpeedUnit.KBPS -> (downloadBytesPerSecond * 8.0) / 1024
            SpeedUnit.KBS -> downloadBytesPerSecond.toDouble() / 1024
        }
    }
    
    fun getUploadSpeed(unit: SpeedUnit): Double {
        return when (unit) {
            SpeedUnit.MBPS -> (uploadBytesPerSecond * 8.0) / (1024 * 1024)
            SpeedUnit.MBS -> uploadBytesPerSecond.toDouble() / (1024 * 1024)
            SpeedUnit.KBPS -> (uploadBytesPerSecond * 8.0) / 1024
            SpeedUnit.KBS -> uploadBytesPerSecond.toDouble() / 1024
        }
    }
}

enum class SpeedUnit(val label: String) {
    MBPS("Mbps"),
    MBS("MB/s"),
    KBPS("Kbps"),
    KBS("KB/s")
}
