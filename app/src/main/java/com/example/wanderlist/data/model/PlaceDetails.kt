package com.example.wanderlist.data.model

/**
 * Represents the metadata for a place photo.
 */
data class PlacePhotoMetadata(
    val photoReference: String,
    val attributions: String?
)

/**
 * Holds all the details for a place as provided by Google Maps.
 */
data class PlaceDetails(
    val id: String,
    val displayName: String?,                       // Place.Field.DISPLAY_NAME
    val openingHours: String?,                      // Place.Field.OPENING_HOURS (convert to string if needed)
    val rating: Double?,                            // Place.Field.RATING
    val formattedAddress: String?,                  // Place.Field.ADR_FORMAT_ADDRESS
    val editorialSummary: String?,                  // Place.Field.EDITORIAL_SUMMARY
    val nationalPhoneNumber: String?,               // Place.Field.NATIONAL_PHONE_NUMBER
    val photoMetadatas: List<PlacePhotoMetadata>?,  // Place.Field.PHOTO_METADATAS
    val websiteUri: String?,                        // Place.Field.WEBSITE_URI (converted to String)
    val accessibilityOptions: List<String>?         // Place.Field.ACCESSIBILITY_OPTIONS
)
