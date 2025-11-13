package com.example.local_network_scanner.services

import com.google.android.gms.maps.model.LatLng

data class GeoIpResult(
    val latLng: LatLng,
    val isoCode: String?
)
