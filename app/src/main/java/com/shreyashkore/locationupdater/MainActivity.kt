package com.shreyashkore.locationupdater

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.shreyashkore.locationupdater.LocationService.Companion.ACTION_LOCATION_BROADCAST
import com.shreyashkore.locationupdater.LocationService.Companion.EXTRA_LOCATION
import com.shreyashkore.locationupdater.ui.Greeting
import com.shreyashkore.locationupdater.ui.MapScreen
import com.shreyashkore.locationupdater.ui.theme.LocationUpdaterTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    private val locationReceiver = LocationReceiver()
    private var locationService: LocationService? = null
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && intent.action == ACTION_LOCATION_BROADCAST) {
                val location = intent.getParcelableExtra<Location>(EXTRA_LOCATION)
                Log.e("Broadcast $$$", location.toString())
                _currentLocation.value = location
            }
        }
    }

//    private val locationServiceConnection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
//            val locationServiceBinder = binder as LocationService.LocationServiceBinder
//            locationService = locationServiceBinder.getService()
//        }
//
//        override fun onServiceDisconnected(name: ComponentName?) {
//            locationService = null
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LocationUpdaterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Greeting(name = "Shreyash")
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
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            broadcastReceiver,
            IntentFilter(ACTION_LOCATION_BROADCAST)
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onPause()
    }

    private fun startTracking() {
        val serviceIntent = Intent(this, LocationService::class.java)
        startService(serviceIntent)
//        bindService(serviceIntent, locationServiceConnection, Context.BIND_AUTO_CREATE)
//        locationService?.subscribeToLocationUpdates()
//        locationReceiver.startReceiver(applicationContext)
    }
}
