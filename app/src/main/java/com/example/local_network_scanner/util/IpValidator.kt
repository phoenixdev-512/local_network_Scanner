package com.example.local_network_scanner.util

/**
 * Validates an IP address using regex pattern
 * @param ip The IP address string to validate
 * @return true if IP is valid, false otherwise
 */
fun isValidIp(ip: String): Boolean {
    if (ip.isBlank()) return false
    val ipRegex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    return ip.matches(ipRegex.toRegex())
}

/**
 * Popular DNS providers for reference
 */
object DnsProviders {
    val CLOUDFLARE_PRIMARY = "1.1.1.1"
    val CLOUDFLARE_SECONDARY = "1.0.0.1"
    
    val GOOGLE_PRIMARY = "8.8.8.8"
    val GOOGLE_SECONDARY = "8.8.4.4"
    
    val QUAD9_PRIMARY = "9.9.9.9"
    val QUAD9_SECONDARY = "149.112.112.112"
    
    val OPENDNS_PRIMARY = "208.67.222.222"
    val OPENDNS_SECONDARY = "208.67.220.220"
}
