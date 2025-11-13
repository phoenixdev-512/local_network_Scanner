package com.example.local_network_scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.datastore.ConnectionRepository
import com.example.local_network_scanner.services.GeoIpService
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val connectionRepository: ConnectionRepository,
    private val geoIpService: GeoIpService
) : ViewModel() {

    private val _coordinates = MutableStateFlow<List<LatLng>>(emptyList())
    val coordinates = _coordinates.asStateFlow()

    init {
        viewModelScope.launch {
            connectionRepository.newConnections
                .sample(1000) // Don't overwhelm the UI, max one update per sec
                .map { ip -> withContext(Dispatchers.IO) { geoIpService.lookupIp(ip) } }
                .filterNotNull()
                .collect { geoIpResult ->
                    _coordinates.value = _coordinates.value + geoIpResult.latLng
                }
        }
    }
}