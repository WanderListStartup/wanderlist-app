// ProfileViewModel.kt
package com.example.wanderlist.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.firestore.repository.UserProfileRepository
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.data.auth.model.AuthDataStore
import com.example.wanderlist.data.firestore.model.Badges
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.auth.User
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.firestore.repository.BadgesRepository
import com.example.wanderlist.data.firestore.model.Reviews
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository
import com.example.wanderlist.data.firestore.repository.ReviewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val establishmentDetailsRepo: EstablishmentDetailsRepository,
    private val badgesRepository: BadgesRepository,
    private val reviewsRepository: ReviewsRepository,
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

    var level by mutableDoubleStateOf(0.0)
        private set

    var friendProfiles by mutableStateOf<List<UserProfile>>(emptyList())
        private set

    var likedEstablishments by mutableStateOf<List<String>>(emptyList())
        private set

    var likedEstablishmentsDetails by mutableStateOf<List<EstablishmentDetails>>(emptyList())
        private set

    var selectedTab by mutableIntStateOf(0)
        private set

    var reviewsForUser by mutableStateOf<List<Reviews>>(emptyList())
        private set

    var establishmentNames by mutableStateOf<Map<String, String>>(emptyMap())
        private set

    var userBadges by mutableStateOf<List<Badges>>(emptyList())
        private set

    var selectedBadges by mutableStateOf<List<Badges>>(emptyList())
        private set

    var slotIndexToSelect by mutableStateOf<Int?>(null)
        private set

    var profilePictureUrl by mutableStateOf("https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG")
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
                selectedBadges = userProfile?.let { badgesRepository.getBadges(it.selectedBadges) } ?: emptyList()
                userBadges = userProfile?.let { badgesRepository.getBadges(it.badges) } ?: emptyList()

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
                    level = it.level
                    likedEstablishments = it.likedEstablishments

                    loadFriendsForCurrentUser(it.friends)
                    likedEstablishmentsDetails =
                        establishmentDetailsRepo.getEstablishmentsDetailsForLargeLists(
                            likedEstablishments
                        )

                    val loadedReviews = reviewsRepository.getReviewsForUser(currentUser.uid)
                    reviewsForUser = loadedReviews

                    loadEstablishmentNamesForReviews(loadedReviews)
                }
                userProfile = profile
                getBadges()
                getSelectedBadges()
            }
        }
    }

    fun updateSelectedTab(index: Int) {
        selectedTab = index
    }

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

    fun updateSelectedBadges(badgeIds: List<String>) {
        viewModelScope.launch {
            val currentUser = authDataStore.getCurrentUser()
            if (currentUser != null) {
                userProfileRepository.updateUserProfile(
                    uid = currentUser.uid,
                    updatedFields = mapOf(
                        "selectedBadges" to badgeIds
                    )
                )
                reloadUserProfile()
            }
        }
    }

    private fun getBadges() {
        viewModelScope.launch {
            val badges = userProfile?.let { badgesRepository.getBadges(it.badges) }
            userBadges = badges!!
            reloadUserProfile()
        }
    }

    private fun getSelectedBadges() {
        viewModelScope.launch {
            val badges = userProfile?.let { badgesRepository.getBadges(it.selectedBadges) }
            selectedBadges = badges!!
            reloadUserProfile()
        }
    }

    fun openBadgeDialog(slotIndex: Int) {
        slotIndexToSelect = slotIndex
    }

    fun closeBadgeDialog() {
        slotIndexToSelect = null
    }

    fun onBadgeSelected(chosenBadge: Badges) {
        val index = slotIndexToSelect ?: return
        val currentBadge = selectedBadges.getOrNull(index)

        // If the chosen badge is already assigned to the active slot, deselect it.
        if (currentBadge != null && currentBadge.badgeId == chosenBadge.badgeId) {
            val currentList = selectedBadges.toMutableList()
            currentList[index] = Badges() // Resets to an empty badge.
            selectedBadges = currentList

            val badgeIds = currentList.filter { it.badgeId.isNotEmpty() }.map { it.badgeId }
            viewModelScope.launch {
                updateSelectedBadges(badgeIds)
                closeBadgeDialog()
            }
            return
        }

        // Otherwise, assign the selected badge to the slot.
        val currentList = selectedBadges.toMutableList()
        while (currentList.size < 4) {
            currentList.add(Badges())
        }
        currentList[index] = chosenBadge
        selectedBadges = currentList

        val badgeIds = currentList.filter { it.badgeId.isNotEmpty() }.map { it.badgeId }
        viewModelScope.launch {
            updateSelectedBadges(badgeIds)
            closeBadgeDialog()
        }
    }




    private fun loadEstablishmentNamesForReviews(reviews: List<Reviews>) {
        viewModelScope.launch {
            // Create a mapping of establishmentId to displayName
            val nameMap = mutableMapOf<String, String>()
            reviews.forEach { review ->
                // Avoid duplicate calls if multiple reviews have the same establishment ID.
                if (!nameMap.containsKey(review.establishmentId)) {
                    val name = establishmentDetailsRepo.getDisplayNameById(review.establishmentId)
                    if (name != null) {
                        nameMap[review.establishmentId] = name
                    }
                }
            }
            // Save this mapping to your view model state.
            establishmentNames = nameMap
        }
    }

    // Example function to delete a review.
    fun deleteReview(reviewId: String, userId: String, establishmentId: String) {
        Log.d("ProfileViewModel", "We are now in the delete reviews")
        viewModelScope.launch {
            try {
                // Delete the review document.
                reviewsRepository.deleteReview(reviewId)
                // Optionally, remove the review uid from the user's profile.
                userProfileRepository.updateUserProfile(userId,
                    mapOf("reviews" to com.google.firebase.firestore.FieldValue.arrayRemove(reviewId))
                )
                // Optionally, also remove the review uid from the establishment's reviews array.
                establishmentDetailsRepo.addReviewToEstablishment(establishmentId, reviewId) // You'll need an analogous removal method here.
                // Log success, refresh state if necessary.
                Log.d(TAG, "Review $reviewId successfully deleted.")
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting review: ${e.message}", e)
            }
        }
    }





}
