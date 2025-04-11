package com.example.wanderlist.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EstablishmentIdHoldViewModel @Inject constructor() : ViewModel() {
    // This will hold the currently selected establishment's ID.
    var selectedEstablishmentId by mutableStateOf("")
        private set

    fun updateSelectedEstablishment(id: String) {
        selectedEstablishmentId = id
    }
}
