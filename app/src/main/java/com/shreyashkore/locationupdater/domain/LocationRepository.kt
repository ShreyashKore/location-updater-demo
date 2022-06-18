package com.shreyashkore.locationupdater.domain

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileWriter
import java.util.concurrent.TimeUnit

class LocationRepository private constructor(private val context: Context) {

    private var nextUpdateTime: Long = 0L

    private val textFile: File by lazy {
        val file = File(context.filesDir, "locations.txt")
        if (!file.exists()) {
            file.createNewFile()
        }
        file
    }

    private val _currentLocation = MutableStateFlow<LocationEntity?>(null)
    val currentLocation: StateFlow<LocationEntity?> = _currentLocation

    fun updateLocation(location: LocationEntity?) {
        _currentLocation.value = location
        appendLocationToFile(location)
    }

    private fun appendLocationToFile(location: LocationEntity?) {
        if (nextUpdateTime > System.currentTimeMillis())
            return

        FileWriter(textFile, true).use {
            it.write("$location\n")
        }
        nextUpdateTime = System.currentTimeMillis() + FIVE_MINUTES
    }

    companion object {
        @Volatile
        private var INSTANCE: LocationRepository? = null

        fun getInstance(context: Context): LocationRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocationRepository(context).also {
                    INSTANCE = it
                }
            }
        }

        internal val FIVE_MINUTES = TimeUnit.MINUTES.toMillis(5)
    }
}