package com.example.wanderlist.data.repository

import com.example.wanderlist.data.model.PlaceDetails
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Plain data class that Firestore can serialize.
 * We flatten LatLng into latitude/longitude doubles.
 */
data class EstablishmentFirestore(
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
    val photoURIs: List<String?>? = null,
    val websiteUri: String? = null
)

class EstablishmentDetailsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    /**
     * Batch‑writes a list of PlaceDetails into "establishment_details".
     */
    suspend fun batchUpload(establishments: List<PlaceDetails>) {
        val batch = firestore.batch()
        establishments.forEach { est ->
            val docRef = firestore.collection("establishment_details").document(est.id)
            val ef = EstablishmentFirestore(
                id = est.id,
                displayName = est.displayName,
                openingHours = est.openingHours,
                rating = est.rating,
                latitude = est.location?.latitude,
                longitude = est.location?.longitude,
                distance = est.distance,
                formattedAddress = est.formattedAddress,
                editorialSummary = est.editorialSummary,
                nationalPhoneNumber = est.nationalPhoneNumber,
                photoURIs = est.photoURIs,
                websiteUri = est.websiteUri
            )
            batch.set(docRef, ef)
        }
        batch.commit().await()
    }
}