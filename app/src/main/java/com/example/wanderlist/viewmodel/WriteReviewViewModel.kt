package com.example.wanderlist.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
    var userRating by mutableFloatStateOf(0f)
        private set
    var reviewText by mutableStateOf("")
        private set
    var postReviewState by mutableStateOf<PostReviewState>(PostReviewState.Idle)
        private set

    fun updateRating(newRating: Float) {
        userRating = newRating
    }

    fun updateReviewText(newText: String) {
        reviewText = newText
    }

    fun postReview(establishmentId: String) {
        viewModelScope.launch {
            val currentUser = authDataStore.getCurrentUser()
            val currentUserUID = currentUser?.uid
            if (currentUser != null && currentUserUID != null) {
                postReviewState = PostReviewState.Posting
                try {
                    val reviewId = UUID.randomUUID().toString()

                    val review = Reviews(
                        id = reviewId,
                        userId = currentUserUID.toString(),
                        establishmentId = establishmentId,
                        rating = userRating,
                        reviewText = reviewText,
                    )

                    reviewsRepository.addReview(review)
                    userProfileRepository.addReviewToUserProfile(currentUserUID.toString(), reviewId)
                    establishmentDetailsRepository.addReviewToEstablishment(establishmentId, reviewId)

                    postReviewState = PostReviewState.Success
                } catch (e: Exception) {
                    postReviewState = PostReviewState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
}
