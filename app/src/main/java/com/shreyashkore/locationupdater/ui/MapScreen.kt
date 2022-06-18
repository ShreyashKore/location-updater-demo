package com.shreyashkore.locationupdater.ui

import android.Manifest
import android.location.Location
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.MapView
import com.shreyashkore.locationupdater.ui.theme.LocationUpdaterTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    startTracking: () -> Unit,
    location: Location?,
) {
    val locationPermission =
        rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

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
                style = MaterialTheme.typography.headlineMedium
            )
            Button(
                onClick = { locationPermission.launchPermissionRequest() }
            ) {
                Text(text = "Grant Permission")
            }
        }
    }
}

@Composable
fun GoogleMap(location: Location?) {
    Text(
        text = "$location"
    )

    AndroidView(
        factory = { ctx ->
            MapView(ctx)
        },
        update = {

        }
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LocationUpdaterTheme {
        Greeting("Android")
    }
}