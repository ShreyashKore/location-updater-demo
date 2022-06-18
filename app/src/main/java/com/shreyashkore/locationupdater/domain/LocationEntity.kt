package com.shreyashkore.locationupdater.domain

import android.location.Location

data class LocationEntity(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val recordedAt: Long
)

fun Location.toLocationEntity() = LocationEntity(
    latitude = latitude,
    longitude = longitude,
    altitude = altitude,
    recordedAt = time
)