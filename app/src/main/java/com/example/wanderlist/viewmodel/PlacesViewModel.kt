package com.example.wanderlist.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.googlemaps.model.PlaceDetails
import com.example.wanderlist.data.googlemaps.repository.PlacesRepository
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository  // ← ADDED
import com.google.firebase.firestore.FirebaseFirestore                      // ← ADDED
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

const val TAG = "PlacesViewModel"

class PlacesViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val repository = PlacesRepository(application.applicationContext)

    // ← ADDED: repository to write PlaceDetails into Firestore
    private val establishmentRepo = EstablishmentDetailsRepository(
        FirebaseFirestore.getInstance()
    )

    private val _places = MutableStateFlow<List<PlaceDetails>>(emptyList())
    val places: StateFlow<List<PlaceDetails>> = _places

    init {
        fetchPlaces()
    }

    private fun fetchPlaces() {
        Log.d(TAG, "fetchPlaces: starting fetch")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "fetchPlaces: starting async repo fetch")
            val placeDetails = repository.fetchAndStorePlaces()
            Log.d(TAG, "fetchPlaces: after async repo fetch got: $placeDetails")
            launch(Dispatchers.Main) {
                _places.value = placeDetails

                // ← ADDED: once we have data, seed Firestore
                seedEstablishments()
            }
        }
    }

    /**
     * ← ADDED:
     * One‑time batch upload of all current PlaceDetails into
     * the 'establishment_details' collection.
     */
    fun seedEstablishments() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = _places.value
                if (list.isNotEmpty()) {
                    establishmentRepo.batchUpload(list)
                    Log.d(TAG, "seedEstablishments: uploaded ${list.size} establishments")
                } else {
                    Log.w(TAG, "seedEstablishments: no places to upload")
                }
            } catch (e: Exception) {
                Log.e(TAG, "seedEstablishments: error uploading establishments", e)
            }
        }
    }
}
