package com.example.wanderlist.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.wanderlist.data.api.PlacesApiService
import com.example.wanderlist.data.model.PlaceDetails
import com.example.wanderlist.data.model.PlacePhotoMetadata
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest
import com.google.gson.Gson
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


fun haversineDistance(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
): Double {
    val R = 6371.0 // Radius of Earth in kilometers

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return R * c
}


class PlacesRepository(private val context: Context) {

    private val apiService = PlacesApiService(context)
    private val gson = Gson()
    private val fileName = "places_data.json"

    private val TAG = "PlacesRepository"

    /**
     * Fetches nearby places via the API (using default parameters),
     * converts them into a list of [PlaceDetails] objects,
     * and stores them locally as a JSON file.
     */
    suspend fun fetchAndStorePlaces(
    ) : List<PlaceDetails>{

        /* Check if the places data is already stored locally.
         * If it is, return it immediately without making an API call.
         */
//        val storedPlaces = getStoredPlaces()
//        if (!storedPlaces.isNullOrEmpty()) {
//            onComplete(storedPlaces)
//            return
//        }

//                try {
//                    // Serialize the list to JSON and write to internal storage.
//                    val json = gson.toJson(placeDetailsList)
//                    context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
//                        outputStream.write(json.toByteArray())
//                    }
//                    onComplete(placeDetailsList)
//                } catch (e: Exception) {
//                    onError(e)
//                }
        Log.d(TAG, "fetchAndStorePlaces: starting getNearbyPlaces")
        val places = apiService.getNearbyPlaces()
        Log.d(TAG, "fetchAndStorePlaces: after getNearbyPlaces got: ${places}")
        val placeDetailsList = places.map { place ->
            PlaceDetails(
                id = place.id ?: "",
                displayName = place.displayName,
                openingHours = place.openingHours?.toString(),
                rating = place.rating,
                location = place.location,
                distance = place.location?.let {
                    place.location?.let {
                        it1 -> haversineDistance(42.731544, -73.682535, it.latitude, it1.longitude)
                    }
                },
                formattedAddress = place.formattedAddress,
                editorialSummary = place.editorialSummary,
                nationalPhoneNumber = place.nationalPhoneNumber,
                photoURIs = place.photoMetadatas?.map { photo ->
                    Log.d(TAG, "fetchAndStorePlaces: before metadatatouri")
                    val r = apiService.photoMetaDataToURI(photo)
                    Log.d(TAG, "fetchAndStorePlaces: after photoMetaDatatouri got: ${r.toString()}")
                    r
                },
                websiteUri = place.websiteUri?.toString()
            )
        }
        return placeDetailsList
    }

    /**
     * Reads the stored JSON file and returns a list of [PlaceDetails] objects.
     */
    fun getStoredPlaces(): List<PlaceDetails>? {
        return try {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) return null
            val json = file.readText()
            gson.fromJson(json, Array<PlaceDetails>::class.java).toList()
        } catch (e: Exception) {
            null
        }
    }
}
