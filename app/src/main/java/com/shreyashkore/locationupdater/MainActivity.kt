package com.shreyashkore.locationupdater

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.shreyashkore.locationupdater.domain.LocationEntity
import com.shreyashkore.locationupdater.domain.LocationRepository
import com.shreyashkore.locationupdater.services.LocationService
import com.shreyashkore.locationupdater.ui.MapScreen
import com.shreyashkore.locationupdater.ui.theme.LocationUpdaterTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {

    var currentLocation: StateFlow<LocationEntity?> = MutableStateFlow(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocationUpdaterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapScreen(
                        startTracking = {
                            startTracking()
                        },
                        location = currentLocation.collectAsState().value
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val repository = LocationRepository.getInstance(applicationContext)
        currentLocation = repository.currentLocation
    }

    private fun startTracking() {
        val serviceIntent = Intent(this, LocationService::class.java)
        startService(serviceIntent)
    }
}
