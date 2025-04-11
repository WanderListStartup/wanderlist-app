package com.example.wanderlist.data.firestore.repository

import android.util.Log
import com.example.wanderlist.data.firestore.model.Category
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.googlemaps.model.PlaceDetails
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class EstablishmentDetailsRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val TAG = "EstablishmentDetailsRepository"
    /**
     * Batch‑writes a list of PlaceDetails into "establishment_details".
     */
    suspend fun batchUpload(establishments: List<PlaceDetails>, category: Category) {
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
                websiteUri = est.websiteUri,
                category = category.displayName
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

    suspend fun getEstablishmentsExcluding(
        excludedIds: List<String>, limit: Int, withFilter: String
    ) : List<EstablishmentDetails>{
        try {
            // Use whereNotIn only if there are fewer than or equal to 10 IDs to filter out.
            val query = if (excludedIds.isEmpty()) {
                Log.d(TAG, "getEstablishmentsExcluding: 1 ${withFilter}")
                firestore.collection("establishment_details").where(Filter.equalTo("category", withFilter)).limit(limit.toLong())
            } else if (excludedIds.size <= 10) {
                Log.d(TAG, "getEstablishmentsExcluding: 2 ${withFilter} exc ${excludedIds}")
                firestore.collection("establishment_details")
                    .whereNotIn("id", excludedIds)
                    .where(Filter.equalTo("category", withFilter))
                    .limit(limit.toLong())
            } else {
                // If more than 10, take only the first 10
                Log.d(TAG, "getEstablishmentsExcluding: 3 ${withFilter} exc ${excludedIds}")
                firestore.collection("establishment_details")
                    .where(Filter.equalTo("category", withFilter))
                    .whereNotIn("id", excludedIds.take(10))
                    .limit(limit.toLong())
            }
            val snapshot = query.get().await()
            val establishments = snapshot.documents.mapNotNull { it.toObject(EstablishmentDetails::class.java) }
            return establishments
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}