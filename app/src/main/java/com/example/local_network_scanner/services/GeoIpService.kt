package com.example.local_network_scanner.services

import android.content.Context
import androidx.annotation.WorkerThread
import com.google.android.gms.maps.model.LatLng
// import com.maxmind.db.DatabaseReader // Temporarily disabled
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.InetAddress
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeoIpService @Inject constructor(@ApplicationContext private val context: Context) {

    /* // Temporarily disabled due to build issues
    private val databaseReader: DatabaseReader =
        context.assets.open("GeoLite2-City.mmdb").use {
            DatabaseReader.Builder(it).build()
        }
    */

    @WorkerThread
    fun lookupIp(ip: String): GeoIpResult? {
        return null // Temporarily disabled
        /*
        return try {
            val response = databaseReader.city(InetAddress.getByName(ip))
            val location = response.location
            val isoCode = response.country.isoCode
            GeoIpResult(LatLng(location.latitude, location.longitude), isoCode)
        } catch (e: Exception) {
            null // IP not found or is local
        }
        */
    }
}