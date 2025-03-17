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

class PlacesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PlacesRepository(application.applicationContext)
    private val _places = MutableStateFlow<List<PlaceDetails>>(emptyList())
    val places: StateFlow<List<PlaceDetails>> = _places

    init {
        fetchPlaces()
    }

    /* Fetches places from the repository and updates the state.
     * This function is called when the ViewModel is initialized.
     * It runs in a coroutine to avoid blocking the main thread.
     */

    private fun fetchPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchAndStorePlaces(
                onComplete = { places ->
                    Log.d("PlacesViewModel", "Fetched and stored ${places.size} places locally")
                    // Retrieve stored places, or fallback to the fetched places.
                    val storedPlaces = repository.getStoredPlaces() ?: places
                    Log.d("PlacesViewModel", "Retrieved ${storedPlaces.size} stored places")

                    // Update state on the main thread.
                    launch(Dispatchers.Main) {
                        _places.value = storedPlaces
                        Log.d("PlacesViewModel", "Places updated in ViewModel")
                    }
                },
                onError = { exception ->
                    Log.e("PlacesViewModel", "Error fetching places: ${exception.message}")
                }
            )
        }
    }

}
