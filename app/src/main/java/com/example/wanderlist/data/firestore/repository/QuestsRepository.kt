package com.example.wanderlist.data.firestore.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class QuestsRepository @Inject constructor (
    private val firestore: FirebaseFirestore
) {
    suspend fun addQuest(establishmentId: String, questName: String) {
        val newQuestRef = firestore.collection("quests").document()
        val questId = newQuestRef.id

        val questData = hashMapOf(
            "questId" to questId,
            "establishmentId" to establishmentId,
            "questName" to questName
        )
        newQuestRef.set(questData).await()
    }

    suspend fun checkHasQuests(establishmentId: String): Boolean {
        val snapshot = firestore.collection("quests")
            .whereEqualTo("establishmentId", establishmentId)
            .get()
            .await()

        return !snapshot.isEmpty
    }
}