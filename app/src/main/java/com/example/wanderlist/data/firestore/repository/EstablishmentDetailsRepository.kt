package com.example.wanderlist.data.firestore.repository

import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.google.model.PlaceDetails
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


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
            val ef = EstablishmentDetails(
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
                photoURIs = est.photoURIs as List<String>?,
                websiteUri = est.websiteUri
            )
            batch.set(docRef, ef)
        }
        batch.commit().await()
    }

    suspend fun getEstablishmentDetails(establishmentId: String): EstablishmentDetails? {


        return try {
            val snapshot = firestore.collection("establishment_details")
                .document(establishmentId)
                .get()
                .await()
            snapshot.toObject(EstablishmentDetails::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}