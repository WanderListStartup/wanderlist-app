package com.example.wanderlist.data.firestore.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.wanderlist.data.firestore.model.UserProfile
import com.google.firebase.firestore.FieldPath


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

    suspend fun getUserProfiles(uids: List<String>): List<UserProfile> {
        if (uids.isEmpty()) {
            return emptyList()
        }

        val userProfiles = mutableListOf<UserProfile>()
        val chunkSize = 10
        // Break the list of UIDs into chunks of size 10
        val uidChunks = uids.chunked(chunkSize)

        for (chunk in uidChunks) {
            val querySnapshot = firestore.collection("user_profiles")
                .whereIn(FieldPath.documentId(), chunk)
                .get()
                .await()

            // Map each document to UserProfile and add to the list
            val profiles = querySnapshot.documents.mapNotNull {
                it.toObject(UserProfile::class.java)
            }
            userProfiles.addAll(profiles)
        }

        return userProfiles
    }

    suspend fun getTenProspectiveFriends(
        searchQuery: String,
        currentUser: UserProfile
    ): List<UserProfile> {
        val querySnapshot = firestore.collection("user_profiles")
            .limit(50)
            .get()
            .await()

        val searchLower = searchQuery.lowercase()

        val excludedIds = mutableSetOf<String>().apply {
            add(currentUser.uid)
            addAll(currentUser.friends)
            addAll(currentUser.incomingRequests)
        }

        val filtered = querySnapshot.documents
            .mapNotNull { it.toObject(UserProfile::class.java) }
            .filter { user ->
                user.uid !in excludedIds &&
                        user.name?.lowercase()?.startsWith(searchLower) == true
            }

        // 5) Return up to 10 results
        return filtered.take(10)
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

    suspend fun getFriends(uid: String) : List<String>?
    {
        return try {
            val snapshot = firestore
                .collection("user_profiles")
                .document(uid)
                .get()
                .await()
            if (snapshot.exists()) {
                snapshot.get("friends") as? List<String>
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getIncomingRequests(uid: String) : List<String>?
    {
        return try {
            val snapshot = firestore
                .collection("user_profiles")
                .document(uid)
                .get()
                .await()
            if (snapshot.exists()) {
                snapshot.get("incomingRequests") as? List<String>
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
