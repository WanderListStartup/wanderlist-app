package com.example.wanderlist.data.firestore.repository

import com.example.wanderlist.data.firestore.model.Quests
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

    suspend fun getAllQuests(establishmentId: String): List<Quests> {
        val snapshot = firestore.collection("quests")
            .whereEqualTo("establishmentId", establishmentId)
            .get()
            .await()

        // Convert QuerySnapshot documents directly to a list of Quest objects.
        return snapshot.toObjects(Quests::class.java)
    }

}