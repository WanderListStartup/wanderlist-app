package com.example.wanderlist.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.auth.model.AuthDataStore
import com.example.wanderlist.data.firestore.model.Reviews
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository
import com.example.wanderlist.data.firestore.repository.ReviewsRepository
import com.example.wanderlist.data.firestore.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed class PostReviewState {
    object Idle : PostReviewState()
    object Posting : PostReviewState()
    object Success : PostReviewState()
    data class Error(val message: String) : PostReviewState()
}

@HiltViewModel
class WriteReviewViewModel @Inject constructor(
    private val reviewsRepository: ReviewsRepository,
    private val userProfileRepository: UserProfileRepository,
    private val establishmentDetailsRepository: EstablishmentDetailsRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    // State variables for the UI:
    var userRating by mutableIntStateOf(0)
        private set
    var reviewText by mutableStateOf("")
        private set
    var postReviewState by mutableStateOf<PostReviewState>(PostReviewState.Idle)
        private set

    fun updateRating(newRating: Int) {
        userRating = newRating
    }

    fun updateReviewText(newText: String) {
        reviewText = newText
    }

    private var existingReview: Reviews? = null

    fun loadExistingReview(establishmentId: String) {
        viewModelScope.launch {
            val currentUser = authDataStore.getCurrentUser()
            if (currentUser != null) {
                existingReview = reviewsRepository.getReviewForUserAndEstablishment(currentUser.uid, establishmentId)
                existingReview?.let { review ->
                    userRating = review.rating
                    reviewText = review.reviewText
                }
            }
        }
    }

    fun postReview(establishmentId: String) {
        viewModelScope.launch {
            val currentUser = authDataStore.getCurrentUser()
            val currentUserUID = currentUser?.uid
            if (currentUser != null && currentUserUID != null) {
                postReviewState = PostReviewState.Posting
                try {
                    if (existingReview != null) {
                        // Update the existing review.
                        val updatedFields = mapOf(
                            "rating" to userRating,
                            "reviewText" to reviewText
                        )
                        reviewsRepository.updateReview(existingReview!!.id, updatedFields)
                        postReviewState = PostReviewState.Success
                    } else {
                        // Create a new review.
                        val reviewId = UUID.randomUUID().toString()
                        val review = Reviews(
                            id = reviewId,
                            userId = currentUserUID,
                            establishmentId = establishmentId,
                            rating = userRating,
                            reviewText = reviewText,
                        )
                        reviewsRepository.addReview(review)
                        // Update user profile and establishment details by adding the review id.
                        userProfileRepository.addReviewToUserProfile(currentUserUID, reviewId)
                        establishmentDetailsRepository.addReviewToEstablishment(establishmentId, reviewId)
                        postReviewState = PostReviewState.Success
                    }
                } catch (e: Exception) {
                    postReviewState = PostReviewState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
}
