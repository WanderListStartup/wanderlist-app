package com.example.wanderlist.data.api

import android.content.Context
import android.util.Log
import com.example.wanderlist.BuildConfig
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import com.google.gson.Gson
import java.io.File


class PlacesApiService(private val context: Context) {

    private val apiKey = BuildConfig.PLACES_API_KEY
    private val gson = Gson()
    private val fileName = "places_data.json"

    // We'll initialize placesClient AFTER we initialize Places
    private val placesClient: PlacesClient

    init {
        // Ensure API key is valid
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            throw IllegalStateException("No API key provided for Places API")
        }
        Places.initializeWithNewPlacesApiEnabled(context, apiKey)
        placesClient = Places.createClient(context)
    }


    fun getNearbyPlaces(callback: (List<Place>) -> Unit, onError: (Exception) -> Unit) {
        val placeFields = listOf(
                Place.Field.ID,
                Place.Field.DISPLAY_NAME,
                Place.Field.OPENING_HOURS,
                Place.Field.RATING,
                Place.Field.ADR_FORMAT_ADDRESS,
                Place.Field.EDITORIAL_SUMMARY,
                Place.Field.NATIONAL_PHONE_NUMBER,
                Place.Field.PHOTO_METADATAS,
                Place.Field.WEBSITE_URI,
                Place.Field.ACCESSIBILITY_OPTIONS
            )

        val center = LatLng(42.731544, -73.682535)
        val circle = CircularBounds.newInstance(center,  /* radius = */1000.0)

        val request = SearchNearbyRequest.builder(circle, placeFields)
            .setMaxResultCount(1)
            .build()

        placesClient.searchNearby(request)
            .addOnSuccessListener { response ->
                Log.d("PlacesApiService", "Raw Response: $response")
                val places = response.places
                Log.d("PlacesApiService", "Places: ${places.size}")
                callback(places)
            }
            .addOnFailureListener { exception ->
                Log.e("PlacesApiService", "Error fetching places: ${exception.message}")
                onError(exception)
            }
    }
}