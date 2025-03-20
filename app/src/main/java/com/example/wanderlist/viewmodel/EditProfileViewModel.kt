package com.example.wanderlist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.R
import com.example.wanderlist.repository.UserProfileRepository
import com.example.wanderlist.repository.UserProfile
import com.example.wanderlist.model.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    // These values will be loaded from Firestore
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

    // For profile picture, you might handle it separately. We'll keep a default.
    var profilePicture by mutableIntStateOf(R.drawable.lebron)
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
                }
            }
        }
    }

    fun onNameChange(newName: String) {
        name = newName
    }

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onBioChange(newBio: String) {
        bio = newBio
    }

    fun onLocationChange(newLocation: String) {
        location = newLocation
    }

    fun onGenderChange(newGender: String) {
        gender = newGender
    }
}
