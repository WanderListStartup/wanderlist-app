// FindFriendsViewModel.kt
package com.example.wanderlist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.data.firestore.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FindFriendsViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    // Already present
    var searchQuery by mutableStateOf("")
        private set

    var allProfiles by mutableStateOf<List<UserProfile>>(emptyList())
        private set

    // Suppose we store the current user’s UID from FirebaseAuth
    var currentUserUid: String? = null
        private set

    init {
        // 1) Load current user’s UID (example from FirebaseAuth)
        currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        // 2) Load all profiles
        viewModelScope.launch {
            allProfiles = userProfileRepository.getAllUserProfiles()
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    // Return the entire list filtered by name/username
    fun filteredProfiles(): List<UserProfile> {
        val q = searchQuery.trim().lowercase()
        return allProfiles.filter {
            it.name.lowercase().contains(q) || it.username.lowercase().contains(q)
        }
    }

    // Returns my current user object, if present
    private fun getCurrentUserProfile(): UserProfile? {
        val me = currentUserUid ?: return null
        return allProfiles.firstOrNull { it.uid == me }
    }

    // -- FRIEND REQUEST LOGIC --

    // The profiles that are in my incomingRequests
    fun incomingRequestProfiles(): List<UserProfile> {
        val me = getCurrentUserProfile() ?: return emptyList()
        val requestUids = me.incomingRequests
        return filteredProfiles().filter { it.uid in requestUids }
    }

    // The rest of the profiles (excluding me + those in my incomingRequests)
    fun otherProfilesExcludingRequests(): List<UserProfile> {
        val me = getCurrentUserProfile() ?: return emptyList()
        val requestUids = me.incomingRequests
        return filteredProfiles().filter {
            it.uid != me.uid && it.uid !in requestUids
        }
    }

    // When I tap "Add Friend" on a user who is NOT an incoming request
    // This sends a request to that user
    fun sendFriendRequest(target: UserProfile) {
        val me = currentUserUid ?: return
        viewModelScope.launch {
            userProfileRepository.updateUserProfile(
                uid = target.uid,
                updatedFields = mapOf(
                    "incomingRequests" to com.google.firebase.firestore.FieldValue.arrayUnion(me)
                )
            )
        }
    }

    // When I tap "Add Friend" on an incoming request user
    // This effectively "accepts" that request
    fun acceptFriendRequest(requesterUid: String) {
        val me = currentUserUid ?: return
        viewModelScope.launch {
            // 1) Remove requester from MY incomingRequests, add them to my friends
            userProfileRepository.updateUserProfile(
                uid = me,
                updatedFields = mapOf(
                    "incomingRequests" to com.google.firebase.firestore.FieldValue.arrayRemove(requesterUid),
                    "friends" to com.google.firebase.firestore.FieldValue.arrayUnion(requesterUid)
                )
            )

            // 2) Add ME to the requester’s friends
            userProfileRepository.updateUserProfile(
                uid = requesterUid,
                updatedFields = mapOf(
                    "friends" to com.google.firebase.firestore.FieldValue.arrayUnion(me)
                )
            )
        }
    }

    fun removeFriendRequest(requesterUid: String) {
        val me = currentUserUid ?: return
        viewModelScope.launch {
            // 1) Remove the dude from friends
            userProfileRepository.updateUserProfile(
                uid = me,
                updatedFields = mapOf(
                    "friends" to FieldValue.arrayRemove(requesterUid)
                )
            )
        }
    }




}

