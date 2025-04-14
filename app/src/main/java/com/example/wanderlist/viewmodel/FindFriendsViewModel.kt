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

    var userProfile by mutableStateOf<UserProfile?>(null)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var incomingRequestProfiles by mutableStateOf<List<UserProfile>>(emptyList())
        private set

    var prospectiveFriends by mutableStateOf<List<UserProfile>>(emptyList())
        private set

    init {
        // 1) Load current user’s UID (example from FirebaseAuth)
        val currUid = FirebaseAuth.getInstance().currentUser?.uid
        // 2) Load the user profile
        currUid?.let { uid ->
            viewModelScope.launch {
                userProfile = userProfileRepository.getUserProfile(uid)
                incomingRequestProfiles = userProfileRepository.getUserProfiles(userProfile?.incomingRequests ?: emptyList())
            }
        }
    }

    private fun reloadUserProfile() {
        val currUid = FirebaseAuth.getInstance().currentUser?.uid
        currUid?.let { uid ->
            viewModelScope.launch {
                userProfile = userProfileRepository.getUserProfile(uid)
                incomingRequestProfiles = userProfileRepository.getUserProfiles(userProfile?.incomingRequests ?: emptyList())
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        viewModelScope.launch {
            searchQuery = newQuery
            prospectiveFriends = if (userProfile != null) {
                userProfileRepository.getTenProspectiveFriends(searchQuery, userProfile!!)
            } else {
                emptyList()
            }
        }

    }

    // When I tap "Add Friend" on a user who is NOT an incoming request
    // This sends a request to that user
    fun sendFriendRequest(friend: UserProfile) {
        val uid = userProfile?.uid ?: return
        viewModelScope.launch {
            userProfileRepository.updateUserProfile(
                uid = friend.uid,
                updatedFields = mapOf(
                    "incomingRequests" to FieldValue.arrayUnion(uid)
                )
            )
        }
        reloadUserProfile()
    }

    // When I tap "Add Friend" on an incoming request user
    // This effectively "accepts" that request
    fun acceptFriendRequest(requesterUid: String) {
        val uid = userProfile?.uid ?: return
        viewModelScope.launch {
            // 1) Remove requester from MY incomingRequests, add them to my friends
            userProfileRepository.updateUserProfile(
                uid = uid,
                updatedFields = mapOf(
                    "incomingRequests" to FieldValue.arrayRemove(requesterUid),
                    "friends" to FieldValue.arrayUnion(requesterUid)
                )
            )

            // 2) Add ME to the requester’s friends
            userProfileRepository.updateUserProfile(
                uid = requesterUid,
                updatedFields = mapOf(
                    "friends" to FieldValue.arrayUnion(uid)
                )
            )
            reloadUserProfile()
        }
    }

    fun removeFriendRequest(requesterUid: String) {
        val uid = userProfile?.uid ?: return
        viewModelScope.launch {
            userProfileRepository.updateUserProfile(
                uid = uid,
                updatedFields = mapOf(
                    "incomingRequests" to FieldValue.arrayRemove(requesterUid)
                )
            )
            reloadUserProfile()
        }
    }
}

