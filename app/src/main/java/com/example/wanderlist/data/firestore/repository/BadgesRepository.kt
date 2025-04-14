package com.example.wanderlist.data.firestore.repository

import android.content.Context
import com.example.wanderlist.data.firestore.model.Badges
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BadgesRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) {

    suspend fun getBadgeId(userLevel: Int) : String {
        val snapshot = firestore.collection("badges")
            .whereEqualTo("levelUnlocked", userLevel)
            .get()
            .await()

        return if (snapshot.documents.isNotEmpty()) {
            snapshot.documents[0].toObject<Badges>()?.badgeId ?: ""
        } else {
            ""
        }
    }

    suspend fun getBadges(badgeIds: List<String>) : List<Badges> {
        val badgeList = mutableListOf<Badges>()
        for (badgeId in badgeIds) {
            val snapshot = firestore.collection("badges")
                .document(badgeId)
                .get()
                .await()

            if (snapshot.exists()) {
                val badge = snapshot.toObject<Badges>()
                if (badge != null) {
                    badgeList.add(badge)
                }
            }
        }
        return badgeList
    }



    // One time function to generate badges
    @Deprecated ("This function is deprecated, and was only used to generate badges once")
    suspend fun generateBadges() {
        // Define the names of each set of three badges
        val badgeNames = listOf("Quest Rookie", "Quest Pro", "Quest Expert", "Quest Master")

        // Define the tiers for each set of three badges
        val badgeTiers = listOf("silver", "bronze", "gold", "emerald")

        // Define the badge images for each tier
        val badgeImages = listOf(
            "https://firebasestorage.googleapis.com/v0/b/wanderlist-b088c.firebasestorage.app/o/silver_badge.png?alt=media&token=284ebb1b-dcd7-434e-841c-1de5b61d47d5",
            "https://firebasestorage.googleapis.com/v0/b/wanderlist-b088c.firebasestorage.app/o/bronze_badge.png?alt=media&token=9888a1b9-32f8-412b-a981-094e3a7d4768",
            "https://firebasestorage.googleapis.com/v0/b/wanderlist-b088c.firebasestorage.app/o/gold_badge.png?alt=media&token=7ade6d8b-0927-45ad-a86c-b6286b2ebfa8",
            "https://firebasestorage.googleapis.com/v0/b/wanderlist-b088c.firebasestorage.app/o/emerald_badge.png?alt=media&token=efa2486f-7c82-427c-9030-cb9bd3fdc95e"
        )

        // For displaying Roman-like suffixes I, II, III
        val romanNumerals = listOf("I", "II", "III")

        // For each group of three levels
        for (groupIndex in 0 until 4) {
            // groupIndex = 0 => levels 1,2,3
            // groupIndex = 1 => levels 4,5,6
            // groupIndex = 2 => levels 7,8,9
            // groupIndex = 3 => levels 10,11,12

            for (j in 1..3) {
                val levelUnlocked = groupIndex * 3 + j
                val name = "${badgeNames[groupIndex]} ${romanNumerals[j - 1]}"

                // Create a new document reference with an auto-generated ID
                val docRef = firestore.collection("badges").document()

                // Build the badge object, injecting the auto-generated ID
                val badge = Badges(
                    badgeId = docRef.id,                // Firestore will auto-generate this ID
                    levelUnlocked = levelUnlocked,
                    tier = badgeTiers[groupIndex],
                    name = name,
                    badgeImageUrl = badgeImages[groupIndex]
                )

                // Upload this badge to Firestore
                docRef.set(badge).await()
            }
        }
    }

}