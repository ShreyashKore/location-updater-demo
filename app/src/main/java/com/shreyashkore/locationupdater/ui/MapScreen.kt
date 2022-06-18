package com.shreyashkore.locationupdater.ui

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.MapView
import com.shreyashkore.locationupdater.domain.LocationEntity
import com.shreyashkore.locationupdater.ui.theme.LocationUpdaterTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    startTracking: () -> Unit,
    location: LocationEntity?,
) {
    val locationPermission =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Greeting(name = "Shreyash")
        when (locationPermission.status) {
            PermissionStatus.Granted -> {
                Button(onClick = startTracking) {
                    Text(text = "Start Tracking")
                }
                GoogleMap(location)
            }
            else -> {
                Text(
                    text = "Location Permission not Available!",
                    style = MaterialTheme.typography.headlineSmall
                )
                Button(
                    onClick = { locationPermission.launchPermissionRequest() }
                ) {
                    Text(text = "Grant Permission")
                }
            }
        }
    }
}

@Composable
fun GoogleMap(location: LocationEntity?) {
    Text(
        text = if (location == null) "Location is not available!"
        else """Location : 
            |Latitude : ${location.latitude}
            |Longitude : ${location.longitude}
            |Altitude : ${location.altitude}
            |""".trimMargin()
    )

    AndroidView(
        factory = { ctx ->
            // MapView not implemented
            MapView(ctx)
        },
        update = {

        }
    )
}

@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello $name!",
        style = MaterialTheme.typography.displayMedium,
        modifier = Modifier.padding(24.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LocationUpdaterTheme {
        MapScreen(
            startTracking = { },
            location = null
        )
    }
}