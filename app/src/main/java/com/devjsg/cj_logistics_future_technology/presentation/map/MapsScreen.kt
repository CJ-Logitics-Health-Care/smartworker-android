package com.devjsg.cj_logistics_future_technology.presentation.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapsScreen() {
    val context = LocalContext.current
    var savedLatitude by remember { mutableStateOf(0.0) }
    var savedLongitude by remember { mutableStateOf(0.0) }

    val initialLatitude = 37.4220
    val initialLongitude = -122.0841
    val initialPosition = LatLng(initialLatitude, initialLongitude)

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