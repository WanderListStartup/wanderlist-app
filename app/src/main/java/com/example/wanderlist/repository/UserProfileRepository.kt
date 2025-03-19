package com.example.wanderlist.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class UserProfile(
    val uid: String,
    val name: String,
    val dob: String,
    val city: String,
    val email: String
)

class UserProfileRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun createUserProfile(userProfile: UserProfile) {
        firestore.collection("user_profiles")
            .document(userProfile.uid)
            .set(userProfile)
            .await()
    }
}
