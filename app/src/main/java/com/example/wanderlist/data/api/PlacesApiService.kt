package com.example.wanderlist.data.api

import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.example.wanderlist.data.model.Place
import com.example.wanderlist.BuildConfig

import android.content.Context
import android.util.Log




class PlacesApiService(context: Context) {

    private val apiKey = BuildConfig.PLACES_API_KEY
    private val placesClient: PlacesClient = Places.createClient(context)


    init {
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("PlacesApiService", "No API key provided")
            throw IllegalStateException("No API key provided for Places API")
        }

        // Initialize the Places SDK with the provided context
        Places.initializeWithNewPlacesApiEnabled(context, apiKey)
    }


    fun getNearbyPlaces(callback: (List<Place>) -> Unit, onError: (Exception) -> Unit) {
        // TODO: Implement the logic to fetch nearby places using the Places API
    }
}
