package com.example.wanderlist.viewmodel

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LikedPlaceViewModel @Inject constructor(
    private val establishmentDetailsRepo: EstablishmentDetailsRepository,
) : ViewModel() {
    var likedEstablishmentDetails by mutableStateOf<EstablishmentDetails?>(null)
        private set


    fun loadLikedEstablishmentDetails(establishmentId: String) {
        viewModelScope.launch {
            likedEstablishmentDetails = establishmentDetailsRepo.getEstablishmentDetails(establishmentId)
        }
    }
}