package com.example.wanderlist.data.firestore.repository

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.wanderlist.data.firestore.model.UserProfile
import com.google.firebase.firestore.toObject
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext


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

    suspend fun putFCMToken(userid : String){
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            firestore.collection("user_profiles")
                .document(userid)
                .update("fcmToken", token)
        } catch (e:Exception){
            Log.e(TAG, "getFCMToken: FAILED TO GET TOKEN", )
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
        return ContextCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED
    }
}
