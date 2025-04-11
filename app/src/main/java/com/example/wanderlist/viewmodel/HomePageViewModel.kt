package com.example.wanderlist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.firestore.model.Category
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository
import com.example.wanderlist.data.firestore.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val establishmentRepository: EstablishmentDetailsRepository,
    private val userProfileRepository: UserProfileRepository,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _establishments = MutableStateFlow<List<EstablishmentDetails>>(emptyList())
    val establishments: StateFlow<List<EstablishmentDetails>> get() = _establishments
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _selectedCategory = MutableStateFlow(Category.FOOD)
    val selectedCategory: StateFlow<Category> = _selectedCategory
    private val _currentPlace = MutableStateFlow<EstablishmentDetails?>(null)
    val currentPlace : StateFlow<EstablishmentDetails?> = _currentPlace

    private val TAG = "HomePageViewModel"

    // Holds the current user profile (used for filtering queries).
    // This is loaded once but will not be updated on swipes.
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> get() = _userProfile

    init {
        viewModelScope.launch {
            Log.d(TAG, ": Launching init ")
            val uid = auth.uid
            if (uid == null){
                Log.e(TAG, "INVALID USER: CHECK CREDENTIALS ", )
                throw RuntimeException("INVALID USER IN HOMEPAGEVIEWMODEL")
            }
            val profile = userProfileRepository.getUserProfile(uid)
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
            Log.d(TAG, "loadMoreEstablishments: starting")
            _isLoading.value = true
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

            Log.d(TAG, "loadMoreEstablishments: filter is ${_selectedCategory.value.displayName}")
            val newPlaces = establishmentRepository.getEstablishmentsExcluding(
                excludedIds = excludeIds,
                limit = 5,
                withFilter = _selectedCategory.value.displayName
            )
            Log.d(TAG, "loadMoreEstablishments: Got ${newPlaces}")
           _establishments.value += newPlaces
            Log.d(TAG, "loadMoreEstablishments: done loading establishments")
            _isLoading.value = false
        }
    }

    fun setSelectedCategory(category: Category) {
        Log.d(TAG, "setSelectedCategory: setting selected category")
        _selectedCategory.value = category
        _establishments.value = emptyList()
        loadMoreEstablishments()
        Log.d(TAG, "setSelectedCategory: done setting selected category")
    }

    /**
     * Removes the first establishment in the list (simulating a card swipe).
     * If the count falls below a threshold, more establishments are loaded.
     * This function is called when a user swipes an establishment to remove it from the list.
     */
    fun removeSwipedEstablishment() {
        viewModelScope.launch {
            Log.d(TAG, "removeSwipedEstablishment: removing")
            refreshUserProfile()
            if (_establishments.value.isNotEmpty()) {
                _establishments.value = _establishments.value.drop(1)
                if (_establishments.value.size < 2) {
                    loadMoreEstablishments()
                }
            }
            Log.d(TAG, "removeSwipedEstablishment: done removing")
        }
    }

    /**
     * Updates the Firestore user profile by adding the establishmentId to the likedEstablishments list.
     * This function is called when a user likes an establishment.
     */
    fun addLikedEstablishment(establishmentId: String) {
        viewModelScope.launch {
            Log.d(TAG, "addLikedEstablishment: adding liked")
            refreshUserProfile()
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
            Log.d(TAG, "addLikedEstablishment: done adding liked")
        }
    }

    /**
     * Updates the Firestore user profile by adding the establishmentId to the dislikedEstablishments list.
     * This function is called when a user dislikes an establishment.
     */
    fun addDislikedEstablishment(establishmentId: String) {
        viewModelScope.launch {
            Log.d(TAG, "addDislikedEstablishment: adding disliked")

            refreshUserProfile()
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
            Log.d(TAG, "addDislikedEstablishment: done adding disliked")
        }
    }
}
