package com.example.wanderlist.utils

import android.content.Context
import com.google.android.gms.location.LocationServices


class LocationHelper(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
}
