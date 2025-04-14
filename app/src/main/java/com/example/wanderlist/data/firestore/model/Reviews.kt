package com.example.wanderlist.data.firestore.model

data class Reviews(
    val id: String = "",
    val userId: String = "",
    val establishmentId: String = "",
    val rating: Int = 0,
    val reviewText: String = "",
)