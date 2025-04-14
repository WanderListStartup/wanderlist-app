package com.example.wanderlist.data.firestore.model

import com.google.firebase.firestore.PropertyName

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val dob: String = "",
    val email: String = "",
    val phone: String = "",
    val isPrivateAccount: Boolean = false,
    val isNotificationsEnabled: Boolean = false,
    val fcmToken: String? = null,
    val username: String = "",
    val bio: String = "",
    val gender: String = "",
    val location: String = "Troy, NY",

    val likedEstablishments: List<String> = emptyList(),
    val dislikedEstablishments: List<String> = emptyList(),
    val reviews: List<String> = emptyList(),
    val quests: List<String> = emptyList(),
    val friends: List<String> = emptyList(),
    val incomingRequests: List<String> = emptyList(),
    val level: Double = 0.0,
)