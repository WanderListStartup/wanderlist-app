package com.example.wanderlist.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.google.model.PlaceDetails
import com.example.wanderlist.data.google.repository.PlacesRepository
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository  // ← ADDED
import com.google.firebase.firestore.FirebaseFirestore                      // ← ADDED
import com.example.wanderlist.data.model.PlaceDetails
import com.example.wanderlist.data.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "PlacesViewModel"


@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val repository: PlacesRepository
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow("Food")
    val selectedCategory: StateFlow<String> = _selectedCategory
    private val _places = MutableStateFlow<List<PlaceDetails>>(emptyList())
    val places: StateFlow<List<PlaceDetails>> = _places
    private val establishmentRepo = EstablishmentDetailsRepository(
        FirebaseFirestore.getInstance()
    )
    private val categoryMapping = mapOf(
        "Food" to "restaurant",
        "Bars" to "bar",
        "Entertainment" to "theatre"
    )

    init {
        val initList = listOf(categoryMapping["Food"]!!)
        fetchPlaces(initList)
    }


    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
        var filterList = listOfNotNull(categoryMapping[category])
        //ERROR NO MATCHING CATEGORY
        if (filterList.isEmpty()){
            Log.e(TAG, "setSelectedCategory: $category does not have a matching string in Category Mapping")
            filterList = listOf("defaultCategory")
        }
        fetchPlaces(filterList)
    }

    private fun fetchPlaces() {
        Log.d(TAG, "fetchPlaces: starting fetch")
    /* Fetches places from the repository and updates the state.
     * This function is called when the ViewModel is initialized.
     * It runs in a coroutine to avoid blocking the main thread.
     */


    private fun fetchPlaces(filters : List<String> = emptyList()) {
        Log.d("PlacesViewModel", "fetchPlaces: startingfetch")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "fetchPlaces: starting async repo fetch")
            val placeDetails = repository.fetchAndStorePlaces(filters)
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
