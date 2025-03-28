package com.example.wanderlist.data.repository

import android.content.Context
import android.util.Log
import com.example.wanderlist.BuildConfig
import com.example.wanderlist.R
import com.example.wanderlist.data.api.PlacesApiService
import com.example.wanderlist.data.model.PlaceDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

const val TAG = "PlacesRepository"

fun haversineDistance(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double,
): Double {
    val r = 6371.0 // Radius of Earth in kilometers

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a =
        sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return r * c
}

class PlacesRepository(
    private val context: Context,
) {
    private val apiService = PlacesApiService(context)
    private val gson = Gson()
    private val fileName = "places_data.json"

    private fun readJsonFromRaw(
        context: Context,
        resourceId: Int,
    ): String {
        val inputStream = context.resources.openRawResource(resourceId)
        val reader = InputStreamReader(inputStream)
        return reader.readText().also { reader.close() }
    }

    private fun parseJsonToPlaces(context: Context): List<PlaceDetails> {
        val json = readJsonFromRaw(context, R.raw.places)
        val gson = Gson()
        val type = object : TypeToken<List<PlaceDetails>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * Fetches nearby places via the API (using default parameters),
     * converts them into a list of [PlaceDetails] objects,
     * and stores them locally as a JSON file.
     */
    suspend fun fetchAndStorePlaces(): List<PlaceDetails> {
        /* Check if the places data is already stored locally.
         * If it is, return it immediately without making an API call.
         */
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "fetchAndStorePlaces: NOT FETCHING APIS!!!! using test data from res/raw/places.json")
            return parseJsonToPlaces(context)
        }
        Log.d(TAG, "fetchAndStorePlaces: starting getNearbyPlaces")
        val places = apiService.getNearbyPlaces()
        Log.d(TAG, "fetchAndStorePlaces: after getNearbyPlaces got: $places")
        val placeDetailsList =
            places.map { place ->
                PlaceDetails(
                    id = place.id ?: "",
                    displayName = place.displayName,
                    openingHours = place.openingHours?.toString(),
                    rating = place.rating,
                    location = place.location,
                    distance =
                        place.location?.let {
                            place.location?.let { it1 ->
                                haversineDistance(42.731544, -73.682535, it.latitude, it1.longitude)
                            }
                        },
                    formattedAddress = place.formattedAddress,
                    editorialSummary = place.editorialSummary,
                    nationalPhoneNumber = place.nationalPhoneNumber,
                    photoURIs =
                        place.photoMetadatas?.map { photo ->
                            Log.d(TAG, "fetchAndStorePlaces: before metadatatouri")
                            val r = apiService.photoMetaDataToURI(photo)
                            Log.d(TAG, "fetchAndStorePlaces: after photoMetaDatatouri got: $r")
                            r.toString()
                        },
                    websiteUri = place.websiteUri?.toString(),
                )
            }

        return placeDetailsList
    }
}
