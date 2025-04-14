package com.example.wanderlist

import com.example.wanderlist.data.firestore.model.Quests
import com.example.wanderlist.data.firestore.repository.QuestsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.Tasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class QuestsRepositoryTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var collection: CollectionReference
    private lateinit var repository: QuestsRepository

    @Before
    fun setUp() {
        firestore = mockk()
        collection = mockk()
        every { firestore.collection("quests") } returns collection
        repository = QuestsRepository(firestore)
    }

    @Test
    fun `addQuest writes new quest with generated id`() = runBlocking {
        // arrange
        val doc = mockk<DocumentReference>()
        every { collection.document() } returns doc
        every { doc.id } returns "generatedQuestId"

        val dataSlot = slot<Map<String, Any>>()
        every { doc.set(capture(dataSlot)) } returns Tasks.forResult(null)

        val estId = "est123"
        val questName = "Find the treasure"

        // act
        repository.addQuest(estId, questName)

        // assert
        verify(exactly = 1) { collection.document() }
        verify(exactly = 1) { doc.set(any()) }

        val data = dataSlot.captured
        assertEquals("generatedQuestId", data["questId"])
        assertEquals(estId, data["establishmentId"])
        assertEquals(questName, data["questName"])
    }

    @Test
    fun `checkHasQuests returns true when snapshot not empty`() = runBlocking {
        // arrange
        val query = mockk<Query>()
        val snapshot = mockk<QuerySnapshot>()
        every { collection.whereEqualTo("establishmentId", "est1") } returns query
        every { query.get() } returns Tasks.forResult(snapshot)
        every { snapshot.isEmpty } returns false

        // act
        val result = repository.checkHasQuests("est1")

        // assert
        assertTrue(result)
        verify { collection.whereEqualTo("establishmentId", "est1") }
        verify { query.get() }
    }

    @Test
    fun `checkHasQuests returns false when snapshot is empty`() = runBlocking {
        // arrange
        val query = mockk<Query>()
        val snapshot = mockk<QuerySnapshot>()
        every { collection.whereEqualTo("establishmentId", "est2") } returns query
        every { query.get() } returns Tasks.forResult(snapshot)
        every { snapshot.isEmpty } returns true

        // act
        val result = repository.checkHasQuests("est2")

        // assert
        assertFalse(result)
    }

    @Test
    fun `getAllQuests returns list of Quests`() = runBlocking {
        // arrange
        val query = mockk<Query>()
        val snapshot = mockk<QuerySnapshot>()
        val questsList = listOf(
            Quests("q1", "est1", "Quest1"),
            Quests("q2", "est1", "Quest2")
        )
        every { collection.whereEqualTo("establishmentId", "est1") } returns query
        every { query.get() } returns Tasks.forResult(snapshot)
        every { snapshot.toObjects(Quests::class.java) } returns questsList

        // act
        val result = repository.getAllQuests("est1")

        // assert
        assertEquals(questsList, result)
    }
}