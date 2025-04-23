package com.example.wanderlist

import com.example.wanderlist.data.firestore.model.Reviews
import com.example.wanderlist.data.firestore.repository.ReviewsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.android.gms.tasks.Tasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class ReviewsRepositoryTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var collection: CollectionReference
    private lateinit var repository: ReviewsRepository

    @Before
    fun setUp() {
        firestore = mockk()
        collection = mockk()
        every { firestore.collection("reviews") } returns collection
        repository = ReviewsRepository(firestore)
    }

    @Test
    fun `addReview calls set on the correct document`() = runBlocking {
        val review = Reviews(
            id = "r1",
            establishmentId = "e1",
            userId = "u1",
            rating = 0,
            reviewText = "hello"
        )
        val docRef = mockk<DocumentReference>()
        every { collection.document(review.id) } returns docRef

        val slotReview = slot<Reviews>()
        every { docRef.set(capture(slotReview)) } returns Tasks.forResult(null)

        repository.addReview(review)

        // assert
        verify(exactly = 1) { collection.document("r1") }
        verify(exactly = 1) { docRef.set(any()) }
        assertEquals(review, slotReview.captured)
    }

    @Test
    fun `getReviewById returns review when document exists`() = runBlocking {
        // arrange
        val reviewId = "r2"
        val docRef = mockk<DocumentReference>()
        val snapshot = mockk<DocumentSnapshot>()
        val expected = Reviews(
            id = reviewId,
            establishmentId = "e2",
            userId = "u2",
            rating = 0,
            reviewText = "hello"
        )

        every { collection.document(reviewId) } returns docRef
        every { docRef.get() } returns Tasks.forResult(snapshot)
        every { snapshot.exists() } returns true
        every { snapshot.toObject(Reviews::class.java) } returns expected

        // act
        val result = repository.getReviewById(reviewId)

        // assert
        assertEquals(expected, result)
        verify { collection.document(reviewId) }
        verify { docRef.get() }
    }

    @Test
    fun `getReviewById returns null when document does not exist`() = runBlocking {
        // arrange
        val reviewId = "r3"
        val docRef = mockk<DocumentReference>()
        val snapshot = mockk<DocumentSnapshot>()

        every { collection.document(reviewId) } returns docRef
        every { docRef.get() } returns Tasks.forResult(snapshot)
        every { snapshot.exists() } returns false

        // act
        val result = repository.getReviewById(reviewId)

        // assert
        assertNull(result)
        verify { collection.document(reviewId) }
        verify { docRef.get() }
    }

    @Test
    fun `getReviewById returns null on exception`() = runBlocking {
        // arrange
        val reviewId = "r4"
        val docRef = mockk<DocumentReference>()

        every { collection.document(reviewId) } returns docRef
        every { docRef.get() } returns Tasks.forException(Exception("Firestore failure"))

        // act
        val result = repository.getReviewById(reviewId)

        // assert
        assertNull(result)
        verify { collection.document(reviewId) }
        verify { docRef.get() }
    }

    @Test
    fun `updateReview calls update on the correct document`() = runBlocking {
        // arrange
        val reviewId = "r5"
        val updates = mapOf<String, Any>(
            "rating" to 3.5,
            "comment" to "Updated comment"
        )
        val docRef = mockk<DocumentReference>()
        every { collection.document(reviewId) } returns docRef
        every { docRef.update(updates) } returns Tasks.forResult(null)

        // act
        repository.updateReview(reviewId, updates)

        // assert
        verify(exactly = 1) { collection.document(reviewId) }
        verify(exactly = 1) { docRef.update(updates) }
    }
}