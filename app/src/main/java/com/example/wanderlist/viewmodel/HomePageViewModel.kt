package com.example.wanderlist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository
import com.example.wanderlist.data.firestore.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomePageViewModel(
    private val establishmentRepository: EstablishmentDetailsRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {
    // Holds the list of establishments to show.
    private val _establishments = MutableStateFlow<List<EstablishmentDetails>>(emptyList())
    val establishments: StateFlow<List<EstablishmentDetails>> get() = _establishments

    // Holds the current user profile (used for filtering queries).
    // This is loaded once but will not be updated on swipes.
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> get() = _userProfile

    init {
        viewModelScope.launch {
            // Replace "user123" with the current user's UID from your authentication system.
            val profile = userProfileRepository.getUserProfile("user123")
            _userProfile.value = profile
            loadMoreEstablishments()
        }
    }

    /**
     * Refreshes the user profile from Firestore. This helps to ensure that the user profile is up-to-date.
     */
    private suspend fun refreshUserProfile() {
        val profile = userProfileRepository.getUserProfile(_userProfile.value?.uid ?: return)
        profile?.let {
            _userProfile.value = it
        }
    }

    /**
     * Loads additional establishments while filtering out those already liked, disliked, or loaded.
     * This function is called upon initialization and when establishments left are less than 2.
     */
    fun loadMoreEstablishments() {
        viewModelScope.launch {
            refreshUserProfile()

            // Gather IDs of already loaded establishments.
            val loadedIds = _establishments.value.map { it.id }
            // Retrieve liked/disliked lists from the user profile.
            val profile = _userProfile.value
            val profileExcludeIds = profile?.let {
                it.likedEstablishments + it.dislikedEstablishments
            } ?: emptyList()
            // Combine IDs that should be excluded.
            val excludeIds = (loadedIds + profileExcludeIds).distinct()

            establishmentRepository.getEstablishmentsExcluding(
                excludedIds = excludeIds,
                limit = 5
            ) { newPlaces ->
                _establishments.value += newPlaces
            }
        }
    }

    /**
     * Removes the first establishment in the list (simulating a card swipe).
     * If the count falls below a threshold, more establishments are loaded.
     * This function is called when a user swipes an establishment to remove it from the list.
     */
    fun removeSwipedEstablishment() {
        viewModelScope.launch {
            if (_establishments.value.isNotEmpty()) {
                _establishments.value = _establishments.value.drop(1)
                if (_establishments.value.size < 2) {
                    loadMoreEstablishments()
                }
            }
        }
    }

    /**
     * Updates the Firestore user profile by adding the establishmentId to the likedEstablishments list.
     * This function is called when a user likes an establishment.
     */
    fun addLikedEstablishment(establishmentId: String) {
        viewModelScope.launch {
            val profile = _userProfile.value
            if (profile == null) {
                Log.e("HomePageViewModel", "User profile is null")
                return@launch
            }
            if (!profile.likedEstablishments.contains(establishmentId)) {
                val updatedLiked = profile.likedEstablishments + establishmentId
                userProfileRepository.updateUserProfile(
                    profile.uid,
                    mapOf("likedEstablishments" to updatedLiked)
                )
            }
        }
    }

    /**
     * Updates the Firestore user profile by adding the establishmentId to the dislikedEstablishments list.
     * This function is called when a user dislikes an establishment.
     */
    fun addDislikedEstablishment(establishmentId: String) {
        viewModelScope.launch {
            val profile = _userProfile.value
            if (profile == null) {
                Log.e("HomePageViewModel", "User profile is null")
                return@launch
            }
            if (!profile.dislikedEstablishments.contains(establishmentId)) {
                val updatedDisliked = profile.dislikedEstablishments + establishmentId
                userProfileRepository.updateUserProfile(
                    profile.uid,
                    mapOf("dislikedEstablishments" to updatedDisliked)
                )
            }
        }
    }
}
