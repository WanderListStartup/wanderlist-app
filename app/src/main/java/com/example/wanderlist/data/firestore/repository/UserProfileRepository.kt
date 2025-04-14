package com.example.wanderlist.data.firestore.repository

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.wanderlist.data.firestore.model.UserProfile
import com.google.firebase.firestore.toObject
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue

class UserProfileRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) {
    private val TAG = "UserProfileRepository"
    suspend fun createUserProfile(userProfile: UserProfile) {
        firestore.collection("user_profiles")
            .document(userProfile.uid)
            .set(userProfile)
            .await()
    }

    suspend fun putFCMToken(userid: String) {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            firestore.collection("user_profiles")
                .document(userid)
                .update("fcmToken", token)
        } catch (e: Exception) {
            Log.e(TAG, "getFCMToken: FAILED TO GET TOKEN",)
        }

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




    fun checkNotificationPermission():Boolean{
        Log.d(TAG, "checkNotificationPermission: ${ContextCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED}")
        return ContextCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED
    }

    suspend fun addReviewToUserProfile(uid: String, reviewId: String) {
        firestore.collection("user_profiles")
            .document(uid)
            .update("reviews", com.google.firebase.firestore.FieldValue.arrayUnion(reviewId))
            .await()
    }

    suspend fun getFriends(uid: String): List<String>? {
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

    suspend fun getIncomingRequests(uid: String): List<String>? {
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

    suspend fun addQuests(uid: String, questId: String) {
        firestore.collection("user_profiles")
            .document(uid)
            .update("quests", FieldValue.arrayUnion(questId))
            .await()
    }

    suspend fun getQuests(uid: String): List<String> {
        return try {
            // Get the user document snapshot
            val documentSnapshot = firestore
                .collection("user_profiles")
                .document(uid)
                .get()
                .await()

            // If the document exists and has a "quests" field, cast it to a List<String>
            if (documentSnapshot.exists()) {
                documentSnapshot.get("quests") as? List<String> ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            // Optionally, log or handle the error here
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getNamebyId(uid: String): String? {
        return try {
            val snapshot = firestore
                .collection("user_profiles")
                .document(uid)
                .get()
                .await()
            if (snapshot.exists()) {
                snapshot.getString("name")
            } else {
                null
            }
        } catch (e: Exception)
        {
            e.printStackTrace()
            null
        }
    }

}

