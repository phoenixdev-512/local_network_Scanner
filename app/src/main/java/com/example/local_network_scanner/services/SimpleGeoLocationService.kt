package com.example.local_network_scanner.services

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Simplified IP geolocation service using bundled IP ranges.
 * This is a lightweight alternative to MaxMind for common countries.
 * 
 * Note: Less accurate than MaxMind but doesn't require external database.
 */
@Singleton
class SimpleGeoLocationService @Inject constructor() {
    
    // Simplified IP ranges for common countries (first octet ranges)
    // This is a very basic implementation - MaxMind is more accurate
    private val countryRanges = mapOf(
        "US" to listOf(3..6, 7..15, 17..19, 23..24, 32..47, 54..63, 64..127, 128..191),
        "CN" to listOf(1..1, 27..27, 36..36, 58..63, 101..101, 106..106, 110..125, 175..175, 202..223),
        "RU" to listOf(2..2, 5..5, 31..31, 37..37, 46..46, 77..95, 109..109, 178..178, 185..185, 188..188),
        "GB" to listOf(2..2, 5..5, 8..8, 25..25, 51..51, 62..62, 77..87, 90..95, 109..109, 151..151),
        "DE" to listOf(2..2, 5..5, 46..46, 62..62, 77..81, 85..95, 109..109, 134..134, 141..141, 176..178, 188..195),
        "FR" to listOf(2..2, 5..5, 37..37, 46..46, 62..62, 77..95, 109..109, 151..151, 176..195),
        "JP" to listOf(1..1, 14..14, 27..27, 43..43, 49..50, 58..63, 106..106, 110..126, 133..133, 153..153, 202..223),
        "KR" to listOf(1..1, 27..27, 58..63, 106..106, 110..126, 175..175, 202..223),
        "IN" to listOf(1..1, 14..14, 27..27, 49..50, 58..63, 101..106, 110..126, 202..223),
        "BR" to listOf(45..45, 131..131, 138..143, 152..152, 177..191, 200..201),
        "CA" to listOf(24..24, 64..70, 99..100, 104..108, 142..142, 192..193, 198..199, 206..207),
        "AU" to listOf(1..1, 14..14, 27..27, 43..43, 49..50, 58..63, 101..103, 110..126, 202..223)
    )
    
    /**
     * Get country code from IP address using simplified range matching.
     * @param ipAddress IP address as string (e.g., "8.8.8.8")
     * @return ISO country code or null if not found
     */
    fun getCountryCode(ipAddress: String): String? {
        try {
            val firstOctet = ipAddress.split(".").firstOrNull()?.toIntOrNull() ?: return null
            
            // Find matching country
            for ((country, ranges) in countryRanges) {
                if (ranges.any { firstOctet in it }) {
                    return country
                }
            }
            
            return null // Unknown country
        } catch (e: Exception) {
            return null
        }
    }
    
    /**
     * Check if this is a private/local IP address.
     */
    fun isPrivateIp(ipAddress: String): Boolean {
        val parts = ipAddress.split(".")
        if (parts.size != 4) return false
        
        val first = parts[0].toIntOrNull() ?: return false
        val second = parts[1].toIntOrNull() ?: return false
        
        return when (first) {
            10 -> true                           // 10.0.0.0/8
            172 -> second in 16..31              // 172.16.0.0/12
            192 -> second == 168                 // 192.168.0.0/16
            127 -> true                           // 127.0.0.0/8 (localhost)
            else -> false
        }
    }
}
