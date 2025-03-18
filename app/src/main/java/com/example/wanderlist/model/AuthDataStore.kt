package com.example.wanderlist.model

import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import androidx.credentials.GetCredentialRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthDataStore(
    private val googleOAuthClientID: String,
    private val auth: FirebaseAuth
) {

    // Google OAuth
    fun getGoogleOAuthClientID(): String {
        return googleOAuthClientID
    }

    fun googleOAuth() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(googleOAuthClientID)
            .setFilterByAuthorizedAccounts(true) // Only show accounts previously used to sign in
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        // Handle the OAuth request (e.g., launch an intent or call a callback)
    }

    // Email/Password Authentication
    suspend fun loginWithEmailAndPassword(email: String, password: String): Result {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(result.user!!)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun registerWithEmailAndPassword(email: String, password: String): Result {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.Success(result.user!!)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun logout() {
        auth.signOut()
    }

    sealed class Result {
        data class Success(val user: FirebaseUser) : Result()
        data class Error(val exception: Exception) : Result()
    }
}