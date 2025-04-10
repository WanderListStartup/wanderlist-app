package com.example.wanderlist.data.googlemaps.api

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.wanderlist.BuildConfig
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import kotlinx.coroutines.tasks.await

const val TAG = "PlacesApiService"

class PlacesApiService(
    context: Context,
) {
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
    suspend fun getNearbyPlaces(): List<Place> {
        // Default configuration parameters.
        Log.d(TAG, "getNearbyPlaces: Starting getNearbyPlaces")
        val defaultFields =
            listOf(
                Place.Field.ID,
                Place.Field.DISPLAY_NAME,
                Place.Field.OPENING_HOURS,
                Place.Field.RATING,
                Place.Field.LOCATION,
                Place.Field.ADR_FORMAT_ADDRESS,
                Place.Field.EDITORIAL_SUMMARY,
                Place.Field.NATIONAL_PHONE_NUMBER,
                Place.Field.PHOTO_METADATAS,
                Place.Field.WEBSITE_URI,
            )
        val defaultCenter = LatLng(42.731544, -73.682535)
        val defaultRadius = 1000.0
        val defaultResultCount = 10

        val circle = CircularBounds.newInstance(defaultCenter, defaultRadius)
        val request =
            SearchNearbyRequest
                .builder(circle, defaultFields)
                .setMaxResultCount(defaultResultCount)
                .build()

        return try {
            Log.d(TAG, "getNearbyPlaces: before awaitcall")
            val r = placesClient.searchNearby(request).await()
            Log.d(TAG, "getNearbyPlaces: after awaitcall received: ${r.places}")
            r.places
        } catch (e: Exception) {
            emptyList<Place>()
        }
    }

    suspend fun photoMetaDataToURI(photoMetadata: PhotoMetadata): Uri? {
        // Get the attribution text and author attributions.
        val attributions = photoMetadata.attributions
        val authorAttributions = photoMetadata.authorAttributions

        // Create a FetchResolvedPhotoUriRequest.
        val photoRequest =
            FetchResolvedPhotoUriRequest
                .builder(photoMetadata)
                .setMaxWidth(500)
                .setMaxHeight(300)
                .build()

        return try {
            Log.d(TAG, "photoMetaDataToURI: before await")
            val r = placesClient.fetchResolvedPhotoUri(photoRequest).await()
            Log.d(TAG, "photoMetaDataToURI: after await got: $r")
            r.uri
        } catch (e: Exception) {
            null
        }
    }
}
