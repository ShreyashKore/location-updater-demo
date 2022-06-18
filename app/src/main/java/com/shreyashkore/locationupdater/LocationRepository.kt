package com.shreyashkore.locationupdater

import android.content.Context
import android.location.Location
import java.io.File
import java.io.FileWriter

class LocationRepository private constructor(private val context: Context) {

    fun addLocation(location: Location) {
        val file = File(context.filesDir, "locations.txt")
        if (!file.exists()) {
            file.createNewFile()
        }

        FileWriter(file, true).use {
            it.write("Location ${System.currentTimeMillis()} : $location\n")
        }

//        context.openFileOutput(file.name, Context.MODE_PRIVATE).use {
//            it.writer
//        }
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
    }
}