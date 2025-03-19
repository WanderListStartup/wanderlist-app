package com.example.wanderlist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import com.example.wanderlist.R

class EditProfileViewModel : ViewModel() {
    // State variables for user profile
    var name by mutableStateOf("Josh")
        private set
    var username by mutableStateOf("imjosh")
        private set
    var bio by mutableStateOf("I like Taco Bell.")
        private set
    var location by mutableStateOf("Troy, NY")
        private set
    var gender by mutableStateOf("Male")
        private set
    var profilePicture by mutableIntStateOf(R.drawable.lebron)
        private set

    // Functions to update state variables
    fun onNameChange(newName: String) {
        name = newName
        Log.d("EditProfileViewModel", "Firestore update: Name changed to $newName")
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
    fun onProfilePictureChange(newImageRes: Int) {
        profilePicture = newImageRes
    }

    fun saveProfile() {
        Log.d("EditProfileViewModel", "Profile saved: name=$name, username=$username, bio=$bio, location=$location, gender=$gender")
        // TODO: Implement the actual save logic, e.g., Firestore update.
    }

}
