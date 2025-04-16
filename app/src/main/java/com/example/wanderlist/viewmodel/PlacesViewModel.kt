package com.example.wanderlist.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.BuildConfig
import com.example.wanderlist.data.firestore.model.Category
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.googlemaps.model.PlaceDetails
import com.example.wanderlist.data.googlemaps.repository.PlacesRepository
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository  // ← ADDED
import com.example.wanderlist.data.firestore.repository.QuestsRepository
import com.example.wanderlist.data.gemini.repository.GeminiRepository
import com.example.wanderlist.data.googlemaps.repository.haversineDistance
import com.google.firebase.firestore.FirebaseFirestore                      // ← ADDED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val TAG = "PlacesViewModel"


@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val repository: PlacesRepository
) : ViewModel() {
    private val establishmentRepo = EstablishmentDetailsRepository(FirebaseFirestore.getInstance())
    private val questsRepo = QuestsRepository(FirebaseFirestore.getInstance())
    private val geminiRepo = GeminiRepository()

    var name by mutableStateOf("")
        private set

    var existingEstablishments by mutableStateOf<List<String>>(emptyList())
        private set

    var newEstablishments by mutableStateOf<List<EstablishmentDetails>>(emptyList())
        private set

    private val categoryMapping = mapOf(
        Category.FOOD to listOf("restaurant"),
        Category.BARS to listOf("bar"),
        Category.ENTERTAINMENT to listOf("park", "plaza", "movie_theater", "zoo", "casino")
    )

    // Keep Commented Out
//    init {
//        findNewEstablishments()
//    }

    fun findNewEstablishments() {
        viewModelScope.launch {
            // 1) Load the IDs of what we’ve already stored
            val currentIds = withContext(Dispatchers.IO) {
                establishmentRepo.getAllEstablishmentIds()
            }
            existingEstablishments = currentIds

            // 2) Prepare a collector for all the new details
            val newlyFound = mutableListOf<EstablishmentDetails>()

            // 3) For each category & its filters
            categoryMapping.forEach { (category, filters) ->
                // fetch raw PlaceDetails (IO)
                val places = withContext(Dispatchers.IO) {
                    repository.fetchAndStorePlaces(filters)
                }

                // keep only those whose id isn’t already in Firestore
                val toAdd = places.filter { it.id !in currentIds }
                if (toAdd.isNotEmpty()) {
                    // 4) write them in batch
                    withContext(Dispatchers.IO) {
                        establishmentRepo.batchUpload(toAdd, category)
                    }
                    // 5) map into our UI model and collect
                    newlyFound += toAdd.map { place ->
                        EstablishmentDetails(
                            id                = place.id,
                            displayName       = place.displayName,
                            openingHours      = place.openingHours,
                            rating            = place.rating,
                            latitude          = place.location?.latitude,
                            longitude         = place.location?.longitude,
                            distance          = place.distance,
                            formattedAddress  = place.formattedAddress,
                            editorialSummary  = place.editorialSummary,
                            nationalPhoneNumber = place.nationalPhoneNumber,
                            photoURIs         = place.photoURIs as List<String>?,
                            websiteUri        = place.websiteUri,
                            category          = category.displayName
                        )
                    }
                }
            }

            // 6) finally, update your state
            newEstablishments = newlyFound
            generateQuests()
        }
    }

    private fun generateQuests() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (newEstablishments.isNotEmpty()) {
                    for (establishment in newEstablishments) {
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
