package com.example.wanderlist.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.firestore.PropertyName

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val dob: String = "",
    val email: String = "",
    val phone: String = "",
    @get:PropertyName("privateAccount")
    @set:PropertyName("privateAccount")
    var isPrivateAccount: Boolean = false,
    @get:PropertyName("notificationsEnabled")
    @set:PropertyName("notificationsEnabled")
    var isNotificationsEnabled: Boolean = false,
    val username: String = "",
    val bio: String = "",
    val gender: String = "",
    val location: String = "Troy, NY",
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
}
