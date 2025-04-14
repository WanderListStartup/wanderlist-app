package com.example.wanderlist.data.firestore.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.wanderlist.data.firestore.model.Reviews

class ReviewsRepository @Inject constructor(
   private val firestore: FirebaseFirestore
) {
    suspend fun addReview(review: Reviews) {
        firestore.collection("reviews")
            .document(review.id)
            .set(review)
            .await()
    }

    suspend fun getReviewById(reviewId: String): Reviews? {
        return try {
            val snapshot = firestore.collection("reviews")
                .document(reviewId)
                .get()
                .await()
            if (snapshot.exists()) {
                snapshot.toObject(Reviews::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateReview(reviewId: String, updatedFields: Map<String, Any>) {
        firestore.collection("reviews")
            .document(reviewId)
            .update(updatedFields)
            .await()
    }

    suspend fun getAllReviewsForEstablishment(establishmentId: String): List<Reviews> {
        return try {
            val snapshot = firestore.collection("reviews")
                .whereEqualTo("establishmentId", establishmentId)
                .get()
                .await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Reviews::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }




}