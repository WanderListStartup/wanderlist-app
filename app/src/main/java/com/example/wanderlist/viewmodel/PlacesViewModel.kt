package com.example.wanderlist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.model.PlaceDetails
import com.example.wanderlist.data.repository.PlacesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import kotlin.math.log

class PlacesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PlacesRepository(application.applicationContext)
    private val _places = MutableStateFlow<List<PlaceDetails>>(emptyList())
    private val TAG = "PlacesViewModel"
    val places: StateFlow<List<PlaceDetails>> = _places

    init {
        fetchPlaces()
    }

    /* Fetches places from the repository and updates the state.
     * This function is called when the ViewModel is initialized.
     * It runs in a coroutine to avoid blocking the main thread.
     */

    private fun fetchPlaces() {
        Log.d("PlacesViewModel", "fetchPlaces: startingfetch")
        viewModelScope.launch(Dispatchers.IO){
            Log.d(TAG, "fetchPlaces: starting async repo fetch")
            val placeDetails = repository.fetchAndStorePlaces()
            Log.d(TAG, "fetchPlaces: after async repo fetch got: $placeDetails")
            launch(Dispatchers.Main){
                Log.d(TAG, "fetchPlaces: updating state")
               _places.value = placeDetails 
            }
        }
    }

}
