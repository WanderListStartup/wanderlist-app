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

    /**
     * Posts the review by:
     *  1. Creating a new review document in the "reviews" collection.
     *  2. Updating the user's profile document with the review id.
     *  3. Updating the establishment document with the review id.
     *
     * @param userId the uid of the reviewer.
     * @param establishmentId the uid of the establishment being reviewed.
     */
    fun postReview(establishmentId: String) {
        println("Posting review with rating: $userRating and text: $reviewText")
        viewModelScope.launch {
            val currentUser = authDataStore.getCurrentUser()
            val currentUserUID = currentUser?.uid
            println("On the if statement cur user and uid: $currentUser, $currentUserUID")
            if (currentUser != null && currentUserUID != null) {
                println("why not printing?")
                println("why not printing?")
                println("why not printing?")
                println("why not printing?")
                println("why not printing?")
                println("why not printing?")
                postReviewState = PostReviewState.Posting
                try {
                    // Generate a unique review id
                    val reviewId = UUID.randomUUID().toString()

                    // Create the Review object.
                    val review = Reviews(
                        id = reviewId,
                        userId = currentUserUID.toString(),
                        establishmentId = establishmentId,
                        rating = userRating,
                        reviewText = reviewText,
                        // You might include reviewText here, if desired
                    )

                    // 1. Add the review document.
                    println("Adding review: $review")
                    reviewsRepository.addReview(review)

                    // 2. Append review id to the user's profile reviews array.
                    println("Adding review ID to user profile: $currentUserUID")
                    userProfileRepository.addReviewToUserProfile(currentUserUID.toString(), reviewId)

                    // 3. Append review id to the establishment's reviews array.
                    println("Adding review ID to establishment: $establishmentId")
                    establishmentDetailsRepository.addReviewToEstablishment(establishmentId, reviewId)

                    postReviewState = PostReviewState.Success
                } catch (e: Exception) {
                    postReviewState = PostReviewState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
}
