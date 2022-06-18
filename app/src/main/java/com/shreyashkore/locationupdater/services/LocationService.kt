package com.shreyashkore.locationupdater.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.shreyashkore.locationupdater.domain.LocationEntity
import com.shreyashkore.locationupdater.domain.LocationRepository
import com.shreyashkore.locationupdater.domain.toLocationEntity
import java.util.concurrent.TimeUnit

class LocationService : Service() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: LocationEntity? = null

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }

    override fun onCreate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRepository = LocationRepository.getInstance(applicationContext)

        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                currentLocation = locationResult.lastLocation?.toLocationEntity()
                Log.i(TAG, "Last Location : " + currentLocation.toString())
                locationRepository.updateLocation(currentLocation)
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    companion object {
        private const val TAG = "LocationService"

        internal const val PACKAGE_NAME = "com.shreyashkore.locationupdater"
        internal const val ACTION_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.LOCATION_BROADCAST"
        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"
    }
}