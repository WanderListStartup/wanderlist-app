package com.example.wanderlist.data.repository

import android.content.Context
import com.example.wanderlist.data.api.PlacesApiService
import com.example.wanderlist.data.model.PlaceDetails
import com.example.wanderlist.data.model.PlacePhotoMetadata
import com.google.gson.Gson
import java.io.File
import android.util.Log

class PlacesRepository(private val context: Context) {

    private val apiService = PlacesApiService(context)
    private val gson = Gson()
    private val fileName = "places_data.json"

    /**
     * Fetches nearby places via the API (using default parameters),
     * converts them into a list of [PlaceDetails] objects,
     * and stores them locally as a JSON file.
     */
    fun fetchAndStorePlaces(
        onComplete: (List<PlaceDetails>) -> Unit,
        onError: (Exception) -> Unit
    ) {

        /* Check if the places data is already stored locally.
         * If it is, return it immediately without making an API call.
         */
        val storedPlaces = getStoredPlaces()
        if (!storedPlaces.isNullOrEmpty()) {
            Log.d("PlacesRepository", "Already stored places found. Returning stored data.")
            onComplete(storedPlaces)
            return
        }

        apiService.getNearbyPlaces(
            callback = { places ->
                // Map raw API Place objects to PlaceDetails objects.
                val placeDetailsList = places.map { place ->
                    PlaceDetails(
                        id = place.id ?: "",
                        displayName = place.displayName,
                        openingHours = place.openingHours?.toString(),
                        rating = place.rating,
                        formattedAddress = place.formattedAddress,
                        editorialSummary = place.editorialSummary,
                        nationalPhoneNumber = place.nationalPhoneNumber,
                        photoMetadatas = place.photoMetadatas?.map { photo ->
                            PlacePhotoMetadata(
                                photoReference = photo.zza(),
                                attributions = photo.attributions,
                                height = photo.height,
                                width = photo.width,
                                authorAttributions = photo.authorAttributions?.toString()
                            )
                        },
                        websiteUri = place.websiteUri?.toString()
                    )
                }
                try {
                    // Serialize the list to JSON and write to internal storage.
                    val json = gson.toJson(placeDetailsList)
                    context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
                        outputStream.write(json.toByteArray())
                    }
                    onComplete(placeDetailsList)
                } catch (e: Exception) {
                    onError(e)
                }
            },
            onError = { exception ->
                onError(exception)
            }
        )
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
