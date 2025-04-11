package com.example.wanderlist.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.auth.model.AuthDataStore
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowMoreViewModel @Inject constructor(
    private val establishmentRepository: EstablishmentDetailsRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    var websiteUrl by mutableStateOf("")
        private set

    var formattedAddress by mutableStateOf("")
        private set

    var nationalPhoneNumber by mutableStateOf("")
        private set

    var openingHours by mutableStateOf("")
        private set

    var photoURIs by mutableStateOf(listOf<String>())
        private set



    fun loadEstablishmentDetails(establishmentId: String) {
        viewModelScope.launch {
            val details = establishmentRepository.getEstablishmentDetails(establishmentId)
            details?.let {
                websiteUrl = it.websiteUri ?: ""
                formattedAddress = it.formattedAddress ?: ""
                nationalPhoneNumber = it.nationalPhoneNumber ?: ""
                openingHours = it.openingHours ?: ""
                photoURIs = it.photoURIs ?: listOf()
            }
        }
    }
}
