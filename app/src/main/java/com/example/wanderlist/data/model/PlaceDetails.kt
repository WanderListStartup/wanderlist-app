package com.example.wanderlist.data.model

import android.net.Uri
import com.google.android.gms.maps.model.LatLng

data class PlacePhotoMetadata(
    // The non-empty photo reference string (from PhotoMetadata.zza()).
    val name: String,
    // The attributions string (from getAttributions()).
    val attributions: String,
    // The height of the photo (from getHeight()).
    val height: Int,
    // The width of the photo (from getWidth()).
    val width: Int,
    // The author attributions, converted to a String if available.
    val authorAttributions: String?
)

//data class Place(
//    val name: String,
//    val rating: Double,
//    val distance: String,
//    val coverImageUrl: String,
//    val aboutText: String,
//    val thumbnailUrls: List<String>
//)

data class PlaceDetails(
    val id: String,
    val displayName: String?,                       // Place.Field.DISPLAY_NAME
    val openingHours: String?,                      // Place.Field.OPENING_HOURS (convert to string if needed)
    val rating: Double?,                            // Place.Field.RATING
    val location: LatLng?,
    val distance : Double?,
    val formattedAddress: String?,                  // Place.Field.ADR_FORMAT_ADDRESS
    val editorialSummary: String?,                  // Place.Field.EDITORIAL_SUMMARY
    val nationalPhoneNumber: String?,               // Place.Field.NATIONAL_PHONE_NUMBER
    val photoURIs: List<Uri?>?,  // Place.Field.PHOTO_METADATAS
    val websiteUri: String?,                        // Place.Field.WEBSITE_URI (converted to String)
)
