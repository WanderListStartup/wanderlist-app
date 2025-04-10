package com.example.wanderlist.data.firestore.model

data class EstablishmentDetails(
    val id: String = "",
    val displayName: String? = null,
    val openingHours: String? = null,
    val rating: Double? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val distance: Double? = null,
    val formattedAddress: String? = null,
    val editorialSummary: String? = null,
    val nationalPhoneNumber: String? = null,
    val photoURIs: List<String>? = null,
    val websiteUri: String? = null,
    val reviews: List<String?>? = null,
)