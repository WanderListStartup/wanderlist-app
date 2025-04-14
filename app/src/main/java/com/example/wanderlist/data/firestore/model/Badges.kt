package com.example.wanderlist.data.firestore.model

data class Badges (
    val badgeId: String = "",
    val levelUnlocked: Int = 0,
    val tier: String = "",
    val name: String = "",
    val badgeImageUrl: String = "",
)