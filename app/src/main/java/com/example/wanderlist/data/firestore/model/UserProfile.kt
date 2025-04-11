package com.example.wanderlist.data.firestore.model

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

    val likedEstablishments: List<String> = emptyList(),
    val reviews: List<String> = emptyList(),
    val friends: List<String> = emptyList(),
    val incomingRequests: List<String> = emptyList(),
    val level: Int = 0,
)