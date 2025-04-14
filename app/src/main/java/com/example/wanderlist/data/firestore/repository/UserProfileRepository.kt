package com.example.wanderlist.data.firestore.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.wanderlist.data.firestore.model.UserProfile
import com.google.firebase.firestore.toObject


class UserProfileRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun createUserProfile(userProfile: UserProfile) {
        firestore.collection("user_profiles")
            .document(userProfile.uid)
            .set(userProfile)
            .await()
    }

    suspend fun getAllUserProfiles(): List<UserProfile> {
        val snapshot = firestore.collection("user_profiles").get().await()
        val list = mutableListOf<UserProfile>()
        for (document in snapshot.documents) {
            document.toObject<UserProfile>()?.let { userProfile ->
                list.add(userProfile)
            }
        }
        return list
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        return try {
            val snapshot = firestore.collection("user_profiles")
                .document(uid)
                .get()
                .await()
            if (snapshot.exists()) {
                snapshot.toObject(UserProfile::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateUserProfile(uid: String, updatedFields: Map<String, Any>) {
        firestore.collection("user_profiles")
            .document(uid)
            .update(updatedFields)
            .await()
    }

    // New function to update quests in the user's subcollection
    suspend fun updateQuests(uid: String, questId: String, isCompleted: Boolean) {
        firestore.collection("user_profiles")
            .document(uid)
            .collection("userQuests")
            .document(questId)
            .update(mapOf("isCompleted" to isCompleted))
            .await()
    }

    suspend fun addReviewToUserProfile(uid: String, reviewId: String) {
        firestore.collection("user_profiles")
            .document(uid)
            .update("reviews", com.google.firebase.firestore.FieldValue.arrayUnion(reviewId))
            .await()
    }

}
