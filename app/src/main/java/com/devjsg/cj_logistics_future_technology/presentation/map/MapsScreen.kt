package com.devjsg.cj_logistics_future_technology.presentation.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsScreen(
    latitude: Double,
    longitude: Double
) {
    var savedLatitude by remember { mutableStateOf(latitude) }
    var savedLongitude by remember { mutableStateOf(longitude) }

    val initialPosition = LatLng(savedLatitude, savedLongitude)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 17f)
    }

    val uiSettings = remember {
        MapUiSettings(myLocationButtonEnabled = true)
    }

    val properties by remember {
        mutableStateOf(MapProperties(isMyLocationEnabled = true))
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {
        if (savedLatitude != 0.0 && savedLongitude != 0.0) {
            Marker(
                state = MarkerState(position = LatLng(savedLatitude, savedLongitude)),
                title = "Saved Location",
                snippet = "Latitude: $savedLatitude, Longitude: $savedLongitude"
            )
        }
    }
}