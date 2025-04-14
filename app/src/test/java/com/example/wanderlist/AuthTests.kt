package com.example.wanderlist

import android.content.Context
import com.example.wanderlist.data.auth.model.AuthDataStore
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AuthTests {
    private lateinit var authDataStore: AuthDataStore
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockContext: Context

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        mockAuth = mockk(relaxed = true)
        mockContext = mockk(relaxed = true)

        authDataStore = AuthDataStore(
            context = mockContext,
            googleOAuthClientID = "fake-client-id",
            auth = mockAuth
        )
    }

    @Test
    fun `registerWithEmailAndPassword returns Success when registration succeeds`() = runTest {
        val mockUser = mockk<FirebaseUser>(relaxed = true)
        val mockResult = mockk<AuthResult> {
            every { user } returns mockUser
        }

        coEvery {
            mockAuth.createUserWithEmailAndPassword(any(), any())
        } returns Tasks.forResult(mockResult)

        val result = authDataStore.registerWithEmailAndPassword("new@email.com", "password123")

        assertTrue(result is AuthDataStore.Result.Success)
        assertEquals(mockUser, (result as AuthDataStore.Result.Success).user)
    }

    @Test
    fun `loginWithEmailAndPassword returns Success when auth succeeds`() = runTest {
        val mockUser = mockk<FirebaseUser>(relaxed = true)
        val mockResult = mockk<AuthResult> {
            every { user } returns mockUser
        }

        coEvery {
            mockAuth.signInWithEmailAndPassword(any(), any())
        } returns Tasks.forResult(mockResult)

        val result = authDataStore.loginWithEmailAndPassword("test@email.com", "password")

        assertTrue(result is AuthDataStore.Result.Success)
        assertEquals(mockUser, (result as AuthDataStore.Result.Success).user)
    }

    @Test
    fun `logout calls FirebaseAuth signOut`() {
        every { mockAuth.signOut() } just Runs

        authDataStore.logout()

        verify { mockAuth.signOut() }
    }


    @Test
    fun `loginWithEmailAndPassword returns Error when auth fails`() = runTest {
        val exception = Exception("Login failed")
        coEvery {
            mockAuth.signInWithEmailAndPassword(any(), any())
        } returns Tasks.forException(exception)

        val result = authDataStore.loginWithEmailAndPassword("test@email.com", "wrongpassword")

        assertTrue(result is AuthDataStore.Result.Error)
        assertEquals("Login failed", (result as AuthDataStore.Result.Error).exception.message)
    }

    @Test
    fun `registerWithEmailAndPassword returns Error when registration fails`() = runTest {
        val exception = Exception("Registration failed")
        coEvery {
            mockAuth.createUserWithEmailAndPassword(any(), any())
        } returns Tasks.forException(exception)

        val result = authDataStore.registerWithEmailAndPassword("new@email.com", "badpass")

        assertTrue(result is AuthDataStore.Result.Error)
        assertEquals("Registration failed", (result as AuthDataStore.Result.Error).exception.message)
    }
}
