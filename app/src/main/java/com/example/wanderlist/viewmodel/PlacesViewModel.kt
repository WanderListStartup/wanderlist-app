package com.example.wanderlist.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.BuildConfig
import com.example.wanderlist.data.googlemaps.model.PlaceDetails
import com.example.wanderlist.data.googlemaps.repository.PlacesRepository
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository  // ← ADDED
import com.example.wanderlist.data.firestore.repository.QuestsRepository
import com.example.wanderlist.data.gemini.repository.GeminiRepository
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
    private val establishmentRepo = EstablishmentDetailsRepository(FirebaseFirestore.getInstance())
    private val questsRepo = QuestsRepository(FirebaseFirestore.getInstance())
    private val geminiRepo = GeminiRepository()

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

                // ONLY RUN SEED ESTABLISHMENTS IF WE CHANGE THE JSON FILE.
                // FUTURE FIX IS TO NOT PUT IN JSON AND UPDATE FIRESTORE DIRECTLY
                // seedEstablishments()
                generateQuests()
            }
        }
    }

    private fun seedEstablishments() {
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

    private fun generateQuests() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val establishments = _places.value
                if (establishments.isNotEmpty()) {
                    for (establishment in establishments) {
                        if (!questsRepo.checkHasQuests(establishment.id)) {
                            val quests = geminiRepo.generateQuests(establishment.displayName)
                            if (quests != null) {
                                Log.d(TAG, "generateQuests: generated quests for ${establishment.displayName}: $quests")
                                for (quest in quests) {
                                    questsRepo.addQuest(establishment.id, quest)
                                }
                            } else {
                                Log.w(TAG, "generateQuests: no quests generated for ${establishment.displayName}")
                            }
                        }

                    }
                } else {
                    Log.w(TAG, "generateQuests: no establishments to generate quests for")
                }

            } catch (e: Exception) {
                Log.e(TAG, "generateQuests: error generating quests", e)
            }
        }
    }
}
