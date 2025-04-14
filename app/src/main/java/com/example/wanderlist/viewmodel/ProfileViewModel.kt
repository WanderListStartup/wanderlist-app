// ProfileViewModel.kt
package com.example.wanderlist.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.firestore.repository.UserProfileRepository
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.data.auth.model.AuthDataStore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.auth.User
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val establishmentDetailsRepo: EstablishmentDetailsRepository,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    var userProfile by mutableStateOf<UserProfile?>(null)
        private set

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

    // Holds the actual friend list for the current user
    var friendProfiles by mutableStateOf<List<UserProfile>>(emptyList())
        private set

    var likedEstablishments by mutableStateOf<List<String>>(emptyList())
        private set

    var likedEstablishmentsDetails by mutableStateOf<List<EstablishmentDetails>>(emptyList())
        private set

    var selectedTab by mutableStateOf(0)
        private set

    init {
        loadUserProfile()  // automatically loads user data and friends
    }

     private fun reloadUserProfile() {
        viewModelScope.launch {
            val currentUser = authDataStore.getCurrentUser()
            if (currentUser != null) {
                userProfile = userProfileRepository.getUserProfile(currentUser.uid)
                loadFriendsForCurrentUser(userProfile?.friends ?: emptyList())
            }
        }
    }

    fun removeFriend(friendUid: String) {
        val uid = userProfile?.uid ?: return
        viewModelScope.launch {

            Log.d("ProfileViewModel", "Removing friend: $friendUid from user: $uid")
            userProfileRepository.updateUserProfile(
                uid = uid,
                updatedFields = mapOf(
                    "friends" to FieldValue.arrayRemove(friendUid)
                )
            )

            userProfileRepository.updateUserProfile(
                uid = friendUid,
                updatedFields = mapOf(
                    "friends" to FieldValue.arrayRemove(uid)
                )
            )
            reloadUserProfile()
        }
    }

    /**
     * Fetch the current user's profile from Firestore,
     * and populate local fields (name, username, etc.).
     * Then also load this user's friend list.
     */
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
                    likedEstablishments = it.likedEstablishments

                    loadFriendsForCurrentUser(it.friends)
                    likedEstablishmentsDetails = establishmentDetailsRepo.getEstablishmentsDetailsForLargeLists(likedEstablishments)
                }
                userProfile = profile
            }
        }
    }

    /**
     * Update the selected tab index.
     */
    fun updateSelectedTab(index: Int) {
        selectedTab = index
    }

    /**
     * Given a list of friend UIDs, fetch each friend's UserProfile from Firestore
     * and store them in [friendProfiles].
     */
    private fun loadFriendsForCurrentUser(friendUids: List<String>) {
        viewModelScope.launch {
            val loadedFriends = mutableListOf<UserProfile>()
            for (friendUid in friendUids) {
                val friendProfile = userProfileRepository.getUserProfile(friendUid)
                if (friendProfile != null) {
                    loadedFriends.add(friendProfile)
                }
            }
            friendProfiles = loadedFriends
        }
    }
}
