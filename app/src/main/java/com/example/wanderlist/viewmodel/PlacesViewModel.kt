package com.example.wanderlist.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            }
        }
    }
}
