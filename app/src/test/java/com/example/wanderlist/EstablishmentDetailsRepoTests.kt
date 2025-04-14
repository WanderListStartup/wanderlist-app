package com.example.wanderlist

import com.example.wanderlist.data.firestore.model.Category
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository
import com.example.wanderlist.data.googlemaps.model.PlaceDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class EstablishmentDetailsRepositoryTest {

    @MockK
    lateinit var firestore: FirebaseFirestore

    @MockK
    lateinit var batch: WriteBatch

    @MockK
    lateinit var collection: CollectionReference

    // will collect the DocumentReferences and EstablishmentDetails passed to batch.set(...)
    private lateinit var docRefList: MutableList<DocumentReference>
    private lateinit var detailsList: MutableList<EstablishmentDetails>

    private lateinit var repository: EstablishmentDetailsRepository

    @Before
    fun setUp() {
        // initialize all @MockK fields
        MockKAnnotations.init(this)

        docRefList = mutableListOf()
        detailsList = mutableListOf()

        every { firestore.batch() } returns batch
        every { firestore.collection("establishment_details") } returns collection

        every { collection.document(any()) } answers {
            val id = firstArg<String>()
            val docRef = mockk<DocumentReference>(relaxed = true)
            every { docRef.id } returns id
            docRef
        }

        every { batch.set(capture(docRefList), capture(detailsList)) } returns batch

        every { batch.commit() } returns Tasks.forResult(null)

        repository = EstablishmentDetailsRepository(firestore)
    }

    @Test
    fun `batchUpload writes all establishments and commits batch`() = runBlocking {
        val place1 = PlaceDetails(
            id = "id1",
            displayName = "Name1",
            openingHours = "9-5",
            rating = 4.5,
            location = LatLng(0.0, 0.0),
            distance = 100.0,
            formattedAddress = "Address1",
            editorialSummary = "Summary1",
            nationalPhoneNumber = "123",
            photoURIs = listOf("uri1"),
            websiteUri = "http://example.com"
        )
        val place2 = PlaceDetails(
            id = "id2",
            displayName = "Name2",
            openingHours = "10-6",
            rating = 3.5,
            location = LatLng(0.0, 0.0),
            distance = 200.0,
            formattedAddress = "Address2",
            editorialSummary = "Summary2",
            nationalPhoneNumber = "456",
            photoURIs = listOf("uri2"),
            websiteUri = "http://example2.com"
        )
        val establishments = listOf(place1, place2)
        val category = Category.FOOD

        repository.batchUpload(establishments, category)

        verify(exactly = 1) { firestore.batch() }
        verify(exactly = 2) { firestore.collection("establishment_details") }

        assertEquals(listOf("id1", "id2"), docRefList.map { it.id })

        val expected1 = EstablishmentDetails(
            id = "id1",
            displayName = "Name1",
            openingHours = "9-5",
            rating = 4.5,
            latitude = 0.0,
            longitude = 0.0,
            distance = 100.0,
            formattedAddress = "Address1",
            editorialSummary = "Summary1",
            nationalPhoneNumber = "123",
            photoURIs = listOf("uri1"),
            websiteUri = "http://example.com",
            category = Category.FOOD.displayName
        )
        val expected2 = EstablishmentDetails(
            id = "id2",
            displayName = "Name2",
            openingHours = "10-6",
            rating = 3.5,
            latitude = 0.0,
            longitude = 0.0,
            distance = 200.0,
            formattedAddress = "Address2",
            editorialSummary = "Summary2",
            nationalPhoneNumber = "456",
            photoURIs = listOf("uri2"),
            websiteUri = "http://example2.com",
            category = Category.FOOD.displayName
        )

        assertEquals(expected1, detailsList[0])
        assertEquals(expected2, detailsList[1])

        verify(exactly = 1) { batch.commit() }
    }
}

class EstablishmentDetailsRepositoryGetTest {

    @MockK
    lateinit var firestore: FirebaseFirestore

    @MockK
    lateinit var collection: CollectionReference

    @MockK
    lateinit var document: DocumentReference

    @MockK
    lateinit var snapshot: DocumentSnapshot

    private lateinit var repository: EstablishmentDetailsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { firestore.collection("establishment_details") } returns collection
        every { collection.document(any()) } returns document
        repository = EstablishmentDetailsRepository(firestore)
    }

    @Test
    fun `getEstablishmentDetails returns object when document exists`() = runBlocking {
        val estId = "est123"
        val expected = EstablishmentDetails(
            id = estId,
            displayName = "Test Place",
            openingHours = "9:00–17:00",
            rating = 4.2,
            latitude = 10.0,
            longitude = 20.0,
            distance = 150.0,
            formattedAddress = "123 Test St",
            editorialSummary = "A nice place.",
            nationalPhoneNumber = "555-1234",
            photoURIs = listOf("uri1", "uri2"),
            websiteUri = "https://example.com",
            category = "TestCategory"
        )

        every { document.get() } returns Tasks.forResult(snapshot)
        every { snapshot.toObject(EstablishmentDetails::class.java) } returns expected
        val result = repository.getEstablishmentDetails(estId)
        assertEquals(expected, result)
        verify { firestore.collection("establishment_details") }
        verify { collection.document(estId) }
        verify { document.get() }
    }

    @Test
    fun `getEstablishmentDetails returns null when get() fails`() = runBlocking {
        val estId = "doesNotExist"
        every { document.get() } returns Tasks.forException(Exception("Firestore error"))
        val result = repository.getEstablishmentDetails(estId)
        assertNull(result)
        verify { firestore.collection("establishment_details") }
        verify { collection.document(estId) }
        verify { document.get() }
    }

    @Test
    fun `getEstablishmentDetails returns null when snapshot has no data`() = runBlocking {
        val estId = "emptyDoc"
        every { document.get() } returns Tasks.forResult(snapshot)
        every { snapshot.toObject(EstablishmentDetails::class.java) } returns null
        val result = repository.getEstablishmentDetails(estId)
        assertNull(result)
        verify { firestore.collection("establishment_details") }
        verify { collection.document(estId) }
        verify { document.get() }
    }
}

class EstablishmentDetailsRepositoryComplexQueriesTest {

    @MockK lateinit var firestore: FirebaseFirestore
    @MockK lateinit var collection: CollectionReference

    private lateinit var repository: EstablishmentDetailsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { firestore.collection("establishment_details") } returns collection
        repository = EstablishmentDetailsRepository(firestore)
    }


    @Test
    fun `getEstablishmentsDetailsForLargeLists empty returns emptyList`() = runBlocking {
        val result = repository.getEstablishmentsDetailsForLargeLists(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getEstablishmentsDetailsForLargeLists small list returns items`() = runBlocking {
        val ids = listOf("a", "b")
        val query = mockk<Query>()
        val snapshot = mockk<QuerySnapshot>()
        val details = listOf(
            EstablishmentDetails("a","NameA","9-5",4.0,1.0,2.0,10.0,"AddrA","SumA","123", listOf("uri"), "web",null, Category.FOOD.displayName),
            EstablishmentDetails("b","NameB","10-6",3.5,3.0,4.0,20.0,"AddrB","SumB","456", listOf("uri2"),"web2",null, Category.FOOD.displayName)
        )

        every { collection.whereIn(FieldPath.documentId(), ids) } returns query
        every { query.get() } returns Tasks.forResult(snapshot)
        every { snapshot.toObjects(EstablishmentDetails::class.java) } returns details

        val result = repository.getEstablishmentsDetailsForLargeLists(ids)

        assertEquals(details, result)
        verify { collection.whereIn(FieldPath.documentId(), ids) }
        verify { query.get() }
    }

    @Test
    fun `getEstablishmentsDetailsForLargeLists large list chunks and aggregates`() = runBlocking {
        val ids = (1..15).map { "id$it" }
        val q1 = mockk<Query>()
        val q2 = mockk<Query>()
        val s1 = mockk<QuerySnapshot>()
        val s2 = mockk<QuerySnapshot>()
        val d1 = listOf(EstablishmentDetails("id1","N1","h",1.0,0.0,0.0,0.0,"","","",null,"Cat"))
        val d2 = listOf(EstablishmentDetails("id11","N11","h",1.0,0.0,0.0,0.0,"","","",null,"Cat"))

        every {
            collection.whereIn(
                FieldPath.documentId(),
                match { it.size == 10 }
            )
        } returns q1
        every {
            collection.whereIn(
                FieldPath.documentId(),
                match { it.size == 5 }
            )
        } returns q2

        every { q1.get() } returns Tasks.forResult(s1)
        every { q2.get() } returns Tasks.forResult(s2)
        every { s1.toObjects(EstablishmentDetails::class.java) } returns d1
        every { s2.toObjects(EstablishmentDetails::class.java) } returns d2

        val result = repository.getEstablishmentsDetailsForLargeLists(ids)

        assertEquals(d1 + d2, result)
        verify {
            collection.whereIn(FieldPath.documentId(), match { it.size == 10 })
            collection.whereIn(FieldPath.documentId(), match { it.size == 5 })
        }
    }


    @Test
    fun `getEstablishmentsExcluding with empty excluded uses category filter and limit`() = runBlocking {
        val limit = 2
        val cat   = "Cat"
        val q1     = mockk<Query>()
        val q2     = mockk<Query>()
        val snap   = mockk<QuerySnapshot>()
        val doc1   = mockk<DocumentSnapshot>()
        val doc2   = mockk<DocumentSnapshot>()
        val e1     = EstablishmentDetails("id1","N1","h",1.0,0.0,0.0,0.0,"","","",null,cat)
        val e2     = EstablishmentDetails("id2","N2","h",1.0,0.0,0.0,0.0,"","","",null,cat)

        every { collection.where(Filter.equalTo("category", cat)) } returns q1
        every { q1.limit(limit.toLong()) } returns q2
        every { q2.get() } returns Tasks.forResult(snap)
        every { snap.documents } returns listOf(doc1, doc2)
        every { doc1.toObject(EstablishmentDetails::class.java) } returns e1
        every { doc2.toObject(EstablishmentDetails::class.java) } returns e2

        val result = repository.getEstablishmentsExcluding(emptyList(), limit, cat)

        assertEquals(listOf(e1, e2), result)
        verify { collection.where(Filter.equalTo("category", cat)) }
        verify { q1.limit(limit.toLong()) }
        verify { q2.get() }
    }

    @Test
    fun `getEstablishmentsExcluding with excluded greater than 10 filters client side`() = runBlocking {
        val excluded = (1..12).map { "id$it" }
        val limit    = 3
        val cat      = "Cat"
        val q1       = mockk<Query>()
        val q2       = mockk<Query>()
        val snap     = mockk<QuerySnapshot>()
        val docs     = excluded.map { id ->
            mockk<DocumentSnapshot>().apply {
                every { toObject(EstablishmentDetails::class.java) } returns EstablishmentDetails(id,"","",null,0.0,0.0,0.0,"","","",null,cat)
            }
        } + listOf(
            mockk<DocumentSnapshot>().apply {
                every { toObject(EstablishmentDetails::class.java) } returns EstablishmentDetails("id13","N13","h",1.0,0.0,0.0,0.0,"","","",null,cat)
            },
            mockk<DocumentSnapshot>().apply {
                every { toObject(EstablishmentDetails::class.java) } returns EstablishmentDetails("id14","N14","h",1.0,0.0,0.0,0.0,"","","",null,cat)
            }
        )

        every { collection.where(Filter.equalTo("category", cat)) } returns q1
        every { q1.limit((limit + excluded.size).toLong()) } returns q2
        every { q2.get() } returns Tasks.forResult(snap)
        every { snap.documents } returns docs

        val result = repository.getEstablishmentsExcluding(excluded, limit, cat)

        // only id13/id14 remain, and then take(limit) => both
        assertEquals(listOf("id13","id14"), result.map { it.id })
        verify { collection.where(Filter.equalTo("category", cat)) }
        verify { q1.limit((limit + excluded.size).toLong()) }
    }

}