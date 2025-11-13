package com.example.local_network_scanner.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.local_network_scanner.ui.viewmodel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val coordinates by viewModel.coordinates.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(20.0, 0.0), 1f) // Center of world
    }

    GoogleMap(cameraPositionState = cameraPositionState) {
        coordinates.forEach { latLng ->
            Marker(
                state = MarkerState(position = latLng),
                title = "Connection"
            )
        }
    }
}
