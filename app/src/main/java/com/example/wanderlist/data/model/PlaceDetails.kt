package com.example.wanderlist.data.model

import com.google.android.gms.maps.model.LatLng

data class PlaceDetails(
    val id: String,
    val displayName: String?, // Place.Field.DISPLAY_NAME
    val openingHours: String?, // Place.Field.OPENING_HOURS (convert to string if needed)
    val rating: Double?, // Place.Field.RATING
    val location: LatLng?,
    val distance: Double?,
    val formattedAddress: String?, // Place.Field.ADR_FORMAT_ADDRESS
    val editorialSummary: String?, // Place.Field.EDITORIAL_SUMMARY
    val nationalPhoneNumber: String?, // Place.Field.NATIONAL_PHONE_NUMBER
    val photoURIs: List<String?>?, // Place.Field.PHOTO_METADATAS
    val websiteUri: String?, // Place.Field.WEBSITE_URI (converted to String)
)
