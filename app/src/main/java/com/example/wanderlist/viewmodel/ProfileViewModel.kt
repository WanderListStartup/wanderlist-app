// ProfileViewModel.kt
package com.example.wanderlist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.firestore.repository.UserProfileRepository
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.data.auth.model.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    var name by mutableStateOf("")
        private set

    var username by mutableStateOf("")
        private set

    var bio by mutableStateOf("")
        private set

    var location by mutableStateOf("")
        private set

    var gender by mutableStateOf("")
        private set

    // Default profile picture URL if none is set
    var profilePictureUrl by mutableStateOf("https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG")
        private set

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val currentUser = authDataStore.getCurrentUser()
            if (currentUser != null) {
                val profile: UserProfile? = userProfileRepository.getUserProfile(currentUser.uid)
                profile?.let {
                    name = it.name
                    username = it.username
                    bio = it.bio
                    location = it.location
                    gender = it.gender
                    // Optionally update profilePictureUrl if your backend stores it
                }
            }
        }
    }
}
