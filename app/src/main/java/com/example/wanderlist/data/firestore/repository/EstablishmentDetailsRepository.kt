package com.example.wanderlist.data.firestore.repository

import com.example.wanderlist.data.firestore.model.Category
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.googlemaps.model.PlaceDetails
import com.google.firebase.firestore.FieldPath
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

    suspend fun getAllEstablishmentIds(): List<String> {
        return try {
            val snapshot = firestore.collection("establishment_details").get().await()
            snapshot.documents.map { it.id }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
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

    suspend fun getEstablishmentsDetailsForLargeLists(establishmentIds: List<String>): List<EstablishmentDetails> {
        if (establishmentIds.isEmpty()) return emptyList()

        return establishmentIds
            .chunked(10)  // Split into sublists of size <= 10
            .flatMap { chunk ->
                try {
                    val snapshot = firestore.collection("establishment_details")
                        .whereIn(FieldPath.documentId(), chunk)
                        .get()
                        .await()

                    snapshot.toObjects(EstablishmentDetails::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList()
                }
            }
    }


    suspend fun getEstablishmentsExcluding(
        excludedIds: List<String>, limit: Int, withFilter: String
    ) : List<EstablishmentDetails>{
        try {
            // Use whereNotIn only if there are fewer than or equal to 10 IDs to filter out.
            val query = if (excludedIds.isEmpty()) {
                firestore.collection("establishment_details").where(Filter.equalTo("category", withFilter)).limit(limit.toLong())
            } else if (excludedIds.size <= 10) {
                firestore.collection("establishment_details")
                    .whereNotIn("id", excludedIds)
                    .where(Filter.equalTo("category", withFilter))
                    .limit(limit.toLong())
            } else {
                // More than 10 excluded IDs. Query by category only,
                // and then we'll do client-side exclusion.
                // Increase limit so we have enough results after exclusion.
                firestore.collection("establishment_details")
                    .where(Filter.equalTo("category", withFilter))
                    // We'll fetch more than 'limit' to offset the excluded items.
                    .limit((limit + excludedIds.size).toLong())
            }
            val snapshot = query.get().await()
            val establishments = snapshot.documents.mapNotNull { it.toObject(EstablishmentDetails::class.java) }

            val filteredEstablishments = if (excludedIds.size > 10) {
                establishments.filterNot { excludedIds.contains(it.id) }
            } else {
                establishments
            }

            return filteredEstablishments.take(limit)
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    suspend fun addReviewToEstablishment(establishmentId: String, reviewId: String) {
        firestore.collection("establishment_details")
            .document(establishmentId)
            .update("reviews", com.google.firebase.firestore.FieldValue.arrayUnion(reviewId))
            .await()
    }

    suspend fun getDisplayNameById(uid: String): String? {
        return try {
            val snapshot = firestore
                .collection("establishment_details")
                .document(uid)
                .get()
                .await()
            if (snapshot.exists()) {
                snapshot.getString("displayName")
            } else {
                null
            }
        } catch (e: Exception)
        {
            e.printStackTrace()
            null
        }
    }

}