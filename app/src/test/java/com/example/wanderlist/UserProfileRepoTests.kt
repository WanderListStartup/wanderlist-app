package com.example.wanderlist

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.data.firestore.repository.UserProfileRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.*
import com.google.firebase.messaging.FirebaseMessaging
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class UserProfileRepositoryTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var context: Context
    private lateinit var repo: UserProfileRepository
    private lateinit var profilesColl: CollectionReference

    @Before
    fun setUp() {
        firestore = mockk()
        context = mockk()
        profilesColl = mockk()
        every { firestore.collection("user_profiles") } returns profilesColl
        repo = UserProfileRepository(firestore, context)
        mockkStatic(FirebaseMessaging::class)
        mockkStatic(ContextCompat::class)
    }
    @Test
    fun `createUserProfile sets document with given profile`() = runBlocking {
        val up = UserProfile(
            uid = "u1",
            name = "Alice",
            friends = listOf("f1"),
            incomingRequests = listOf("r1"),
            quests = listOf("q1"),
            reviews = listOf("rev1")
        )
        val doc = mockk<DocumentReference>()
        every { profilesColl.document(up.uid) } returns doc
        every { doc.set(up) } returns Tasks.forResult(null)

        repo.createUserProfile(up)

        verify { profilesColl.document("u1") }
        verify { doc.set(up) }
    }
    @Test
    fun `putFCMToken updates token when fetched`() = runBlocking {
        val userId = "u2"
        val doc = mockk<DocumentReference>()
        every { profilesColl.document(userId) } returns doc

        val fm = mockk<FirebaseMessaging>()
        every { FirebaseMessaging.getInstance() } returns fm
        every { fm.token } returns Tasks.forResult("tok123")
        every { doc.update("fcmToken", "tok123") } returns Tasks.forResult(null)

        repo.putFCMToken(userId)

        verify { FirebaseMessaging.getInstance() }
        verify { fm.token }
        verify { doc.update("fcmToken", "tok123") }
    }
    @Test
    fun `getAllUserProfiles returns list of profiles`() = runBlocking {
        val snap = mockk<QuerySnapshot>()
        val d1 = mockk<DocumentSnapshot>()
        val d2 = mockk<DocumentSnapshot>()
        every { profilesColl.get() } returns Tasks.forResult(snap)
        every { snap.documents } returns listOf(d1, d2)
        val p1 = UserProfile(
            uid = "123",
            name = "Alice",
            dob = "2000-01-01",
            email = "alice@example.com",
            phone = "1234567890",
            isPrivateAccount = false,
            isNotificationsEnabled = true,
            fcmToken = "token_abc",
            username = "alice123",
            bio = "Hello there!",
            gender = "Female",
            location = "Troy, NY",
            likedEstablishments = listOf("est1"),
            dislikedEstablishments = listOf("est2"),
            reviews = listOf("rev1"),
            quests = listOf("q1"),
            friends = listOf("uid2"),
            incomingRequests = listOf("uid3"),
            level = 5
        )
        val p2 = UserProfile(
            uid = "1234",
            name = "Alice2",
            dob = "2000-01-01",
            email = "alice@example.com",
            phone = "1234567890",
            isPrivateAccount = false,
            isNotificationsEnabled = true,
            fcmToken = "token_abc",
            username = "alice123",
            bio = "Hello there!",
            gender = "Female",
            location = "Troy, NY",
            likedEstablishments = listOf("est1"),
            dislikedEstablishments = listOf("est2"),
            reviews = listOf("rev1"),
            quests = listOf("q1"),
            friends = listOf("uid2"),
            incomingRequests = listOf("uid3"),
            level = 5
        )
        every { d1.toObject(UserProfile::class.java) } returns p1
        every { d2.toObject(UserProfile::class.java) } returns p2

        val result = repo.getAllUserProfiles()

        assertEquals(listOf(p1, p2), result)
    }
    @Test
    fun `getUserProfile returns profile when exists`() = runBlocking {
        val uid = "u4"
        val doc = mockk<DocumentReference>()
        val snap = mockk<DocumentSnapshot>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forResult(snap)
        every { snap.exists() } returns true
        val expected = UserProfile(
            uid = "123",
            name = "Alice",
            dob = "2000-01-01",
            email = "alice@example.com",
            phone = "1234567890",
            isPrivateAccount = false,
            isNotificationsEnabled = true,
            fcmToken = "token_abc",
            username = "alice123",
            bio = "Hello there!",
            gender = "Female",
            location = "Troy, NY",
            likedEstablishments = listOf("est1"),
            dislikedEstablishments = listOf("est2"),
            reviews = listOf("rev1"),
            quests = listOf("q1"),
            friends = listOf("uid2"),
            incomingRequests = listOf("uid3"),
            level = 5
        )
        every { snap.toObject(UserProfile::class.java) } returns expected

        val result = repo.getUserProfile(uid)

        assertEquals(expected, result)
    }

    @Test
    fun `getUserProfile returns null when not exists`() = runBlocking {
        val uid = "u5"
        val doc = mockk<DocumentReference>()
        val snap = mockk<DocumentSnapshot>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forResult(snap)
        every { snap.exists() } returns false

        val result = repo.getUserProfile(uid)

        assertNull(result)
    }

    @Test
    fun `getUserProfile returns null on exception`() = runBlocking {
        val uid = "u6"
        val doc = mockk<DocumentReference>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forException(Exception("err"))

        val result = repo.getUserProfile(uid)

        assertNull(result)
    }
    @Test
    fun `getUserProfiles empty uids returns emptyList`() = runBlocking {
        val result = repo.getUserProfiles(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getUserProfiles small list returns profiles`() = runBlocking {
        val uids = listOf("a","b")
        val query = mockk<Query>()
        val snap = mockk<QuerySnapshot>()
        val d1 = mockk<DocumentSnapshot>()
        val d2 = mockk<DocumentSnapshot>()
        every { profilesColl.whereIn(FieldPath.documentId(), uids) } returns query
        every { query.get() } returns Tasks.forResult(snap)
        every { snap.documents } returns listOf(d1, d2)
        val p1 = UserProfile(
            uid = "123",
            name = "Alice",
            dob = "2000-01-01",
            email = "alice@example.com",
            phone = "1234567890",
            isPrivateAccount = false,
            isNotificationsEnabled = true,
            fcmToken = "token_abc",
            username = "alice123",
            bio = "Hello there!",
            gender = "Female",
            location = "Troy, NY",
            likedEstablishments = listOf("est1"),
            dislikedEstablishments = listOf("est2"),
            reviews = listOf("rev1"),
            quests = listOf("q1"),
            friends = listOf("uid2"),
            incomingRequests = listOf("uid3"),
            level = 5
        )
        val p2 = UserProfile(
            uid = "1234",
            name = "Alice1",
            dob = "2000-01-01",
            email = "alice@example.com",
            phone = "1234567890",
            isPrivateAccount = false,
            isNotificationsEnabled = true,
            fcmToken = "token_abc",
            username = "alice123",
            bio = "Hello there!",
            gender = "Female",
            location = "Troy, NY",
            likedEstablishments = listOf("est1"),
            dislikedEstablishments = listOf("est2"),
            reviews = listOf("rev1"),
            quests = listOf("q1"),
            friends = listOf("uid2"),
            incomingRequests = listOf("uid3"),
            level = 5
        )
        every { d1.toObject(UserProfile::class.java) } returns p1
        every { d2.toObject(UserProfile::class.java) } returns p2

        val result = repo.getUserProfiles(uids)

        assertEquals(listOf(p1, p2), result)
    }

    @Test
    fun `getUserProfiles large list chunks and aggregates`() = runBlocking {
        val uids = (1..12).map { "id$it" }
        val q1 = mockk<Query>(); val q2 = mockk<Query>()
        val s1 = mockk<QuerySnapshot>(); val s2 = mockk<QuerySnapshot>()
        every {
            profilesColl.whereIn(FieldPath.documentId(), match { it.size == 10 })
        } returns q1
        every {
            profilesColl.whereIn(FieldPath.documentId(), match { it.size == 2 })
        } returns q2
        every { q1.get() } returns Tasks.forResult(s1)
        every { q2.get() } returns Tasks.forResult(s2)
        val p1 = UserProfile(
            uid = "123",
            name = "Alice",
            dob = "2000-01-01",
            email = "alice@example.com",
            phone = "1234567890",
            isPrivateAccount = false,
            isNotificationsEnabled = true,
            fcmToken = "token_abc",
            username = "alice123",
            bio = "Hello there!",
            gender = "Female",
            location = "Troy, NY",
            likedEstablishments = listOf("est1"),
            dislikedEstablishments = listOf("est2"),
            reviews = listOf("rev1"),
            quests = listOf("q1"),
            friends = listOf("uid2"),
            incomingRequests = listOf("uid3"),
            level = 5
        )
        val p2 = UserProfile(
            uid = "1234",
            name = "Alice1",
            dob = "2000-01-01",
            email = "alice@example.com",
            phone = "1234567890",
            isPrivateAccount = false,
            isNotificationsEnabled = true,
            fcmToken = "token_abc",
            username = "alice123",
            bio = "Hello there!",
            gender = "Female",
            location = "Troy, NY",
            likedEstablishments = listOf("est1"),
            dislikedEstablishments = listOf("est2"),
            reviews = listOf("rev1"),
            quests = listOf("q1"),
            friends = listOf("uid2"),
            incomingRequests = listOf("uid3"),
            level = 5
        )
        val d1 = mockk<DocumentSnapshot>(); every { d1.toObject(UserProfile::class.java) } returns p1
        val d2 = mockk<DocumentSnapshot>(); every { d2.toObject(UserProfile::class.java) } returns p2
        every { s1.documents } returns listOf(d1)
        every { s2.documents } returns listOf(d2)

        val result = repo.getUserProfiles(uids)

        assertEquals(listOf(p1,p2), result)
    }

    // ----------------------------------------------------
    // getTenProspectiveFriends
    // ----------------------------------------------------

    // ----------------------------------------------------
    // updateUserProfile
    // ----------------------------------------------------
    @Test
    fun `updateUserProfile calls update with given fields`() = runBlocking {
        val uid = "u7"
        val updates = mapOf("name" to "NewName")
        val doc = mockk<DocumentReference>()
        every { profilesColl.document(uid) } returns doc
        every { doc.update(updates) } returns Tasks.forResult(null)

        repo.updateUserProfile(uid, updates)

        verify { doc.update(updates) }
    }

    @Test
    fun `updateQuests updates isCompleted field in subcollection`() = runBlocking {
        val uid = "u8"; val questId = "q1"
        val userDoc = mockk<DocumentReference>()
        val subColl = mockk<CollectionReference>()
        val questDoc = mockk<DocumentReference>()
        every { profilesColl.document(uid) } returns userDoc
        every { userDoc.collection("userQuests") } returns subColl
        every { subColl.document(questId) } returns questDoc
        every { questDoc.update(mapOf("isCompleted" to true)) } returns Tasks.forResult(null)

        repo.updateQuests(uid, questId, true)

        verify { questDoc.update(mapOf("isCompleted" to true)) }
    }

    @Test
    fun `getFriends returns list when exists`() = runBlocking {
        val uid = "u10"
        val doc = mockk<DocumentReference>()
        val snap = mockk<DocumentSnapshot>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forResult(snap)
        every { snap.exists() } returns true
        every { snap.get("friends") } returns listOf("f1","f2")

        val result = repo.getFriends(uid)
        assertEquals(listOf("f1","f2"), result)
    }

    @Test
    fun `getFriends returns null when not exists`() = runBlocking {
        val uid = "u11"
        val doc = mockk<DocumentReference>()
        val snap = mockk<DocumentSnapshot>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forResult(snap)
        every { snap.exists() } returns false

        assertNull(repo.getFriends(uid))
    }

    @Test
    fun `getFriends returns null on exception`() = runBlocking {
        val uid = "u12"
        val doc = mockk<DocumentReference>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forException(Exception("err"))

        assertNull(repo.getFriends(uid))
    }

    @Test
    fun `getIncomingRequests returns list when exists`() = runBlocking {
        val uid = "u13"
        val doc = mockk<DocumentReference>()
        val snap = mockk<DocumentSnapshot>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forResult(snap)
        every { snap.exists() } returns true
        every { snap.get("incomingRequests") } returns listOf("r1","r2")

        val result = repo.getIncomingRequests(uid)
        assertEquals(listOf("r1","r2"), result)
    }

    @Test
    fun `getIncomingRequests returns null when not exists`() = runBlocking {
        val uid = "u14"
        val doc = mockk<DocumentReference>()
        val snap = mockk<DocumentSnapshot>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forResult(snap)
        every { snap.exists() } returns false

        assertNull(repo.getIncomingRequests(uid))
    }

    @Test
    fun `getIncomingRequests returns null on exception`() = runBlocking {
        val uid = "u15"
        val doc = mockk<DocumentReference>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forException(Exception("err"))

        assertNull(repo.getIncomingRequests(uid))
    }

    @Test
    fun `getQuests returns list when exists`() = runBlocking {
        val uid = "u17"
        val doc = mockk<DocumentReference>()
        val snap = mockk<DocumentSnapshot>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forResult(snap)
        every { snap.exists() } returns true
        every { snap.get("quests") } returns listOf("q1","q2")

        val result = repo.getQuests(uid)
        assertEquals(listOf("q1","q2"), result)
    }

    @Test
    fun `getQuests returns empty when field missing`() = runBlocking {
        val uid = "u18"
        val doc = mockk<DocumentReference>()
        val snap = mockk<DocumentSnapshot>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forResult(snap)
        every { snap.exists() } returns true
        every { snap.get("quests") } returns null

        val result = repo.getQuests(uid)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getQuests returns empty when not exists`() = runBlocking {
        val uid = "u19"
        val doc = mockk<DocumentReference>()
        val snap = mockk<DocumentSnapshot>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forResult(snap)
        every { snap.exists() } returns false

        val result = repo.getQuests(uid)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getQuests returns empty on exception`() = runBlocking {
        val uid = "u20"
        val doc = mockk<DocumentReference>()
        every { profilesColl.document(uid) } returns doc
        every { doc.get() } returns Tasks.forException(Exception("err"))

        val result = repo.getQuests(uid)
        assertTrue(result.isEmpty())
    }
}