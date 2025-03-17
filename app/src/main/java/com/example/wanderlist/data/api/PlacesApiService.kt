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


class PlacesApiService(context: Context) {

    private val apiKey = BuildConfig.PLACES_API_KEY
    private val placesClient: PlacesClient

    init {
        // Ensure API key is valid
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            throw IllegalStateException("No API key provided for Places API")
        }
        Places.initializeWithNewPlacesApiEnabled(context, apiKey)
        placesClient = Places.createClient(context)
    }


    /**
     * Fetches nearby places with a pre-defined configuration.
     */
    fun getNearbyPlaces(callback: (List<Place>) -> Unit, onError: (Exception) -> Unit) {
        // Default configuration parameters.
        val defaultFields = listOf(
            Place.Field.ID,
            Place.Field.DISPLAY_NAME,
            Place.Field.OPENING_HOURS,
            Place.Field.RATING,
            Place.Field.ADR_FORMAT_ADDRESS,
            Place.Field.EDITORIAL_SUMMARY,
            Place.Field.NATIONAL_PHONE_NUMBER,
            Place.Field.PHOTO_METADATAS,
            Place.Field.WEBSITE_URI
        )
        val defaultCenter = LatLng(42.731544, -73.682535)
        val defaultRadius = 1000.0
        val defaultResultCount = 1

        getNearbyPlaces(
            center = defaultCenter,
            radius = defaultRadius,
            placeFields = defaultFields,
            maxResultCount = defaultResultCount,
            callback = callback,
            onError = onError
        )
    }

    /**
     * Fetches nearby places based on the input parameters.
     *
     * @param center The center coordinate for the search.
     * @param radius The search radius (in meters) around the center.
     * @param placeFields A list of [Place.Field] to request.
     * @param maxResultCount The maximum number of results to return.
     * @param callback Called with the list of places on success.
     * @param onError Called with the exception on failure.
     */
    fun getNearbyPlaces(
        center: LatLng,
        radius: Double,
        placeFields: List<Place.Field>,
        maxResultCount: Int,
        callback: (List<Place>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val circle = CircularBounds.newInstance(center, radius)
        val request = SearchNearbyRequest.builder(circle, placeFields)
            .setMaxResultCount(maxResultCount)
            .build()

        placesClient.searchNearby(request)
            .addOnSuccessListener { response ->
                Log.d("PlacesApiService", "Raw Response: $response")
                callback(response.places)
            }
            .addOnFailureListener { exception ->
                Log.e("PlacesApiService", "Error fetching places: ${exception.message}")
                onError(exception)
            }
    }
}