package com.shreyashkore.locationupdater

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import com.shreyashkore.locationupdater.LocationService.Companion.PACKAGE_NAME

class LocationReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_UPDATES) {
            return
        }

        LocationAvailability.extractLocationAvailability(intent)?.let {
            if (!it.isLocationAvailable) {
                Log.d(TAG, "Location services are no longer available!")
//                return
            }
        }

        LocationResult.extractResult(intent)?.let { locationResult ->
            locationResult.lastLocation?.let {
                LocationRepository.getInstance(context).addLocation(it)
            }
        }

    }

    fun startReceiver(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, LocationReceiver::class.java)
        val broadcast = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val interval = 80000L
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            interval,
            broadcast
        )
    }

    companion object {
        const val TAG = "LOCATION_RECEIVER"
        const val ACTION_UPDATES = "$PACKAGE_NAME.action.PROCESS_UPDATES"
        const val REQUEST_CODE = 23
    }
}