// AuthDataStore.kt
package com.example.wanderlist.model

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val googleOAuthClientID: String,
    private val auth: FirebaseAuth
) {
    private var credentialManager: CredentialManager = CredentialManager.create(context)

    // Return the injected Google OAuth client ID.
    fun getGoogleOAuthClientID(): String = googleOAuthClientID

        suspend fun startGoogleOAuth(request: GetCredentialRequest): Result {
            try {
                val result = credentialManager.getCredential(context, request)
                return handleGoogleSignIn(result.credential)
            } catch (e: Exception) {
                Log.d("GoogleOAuth", "startGoogleOAuth: getcred " + e.message)
                return Result.Error(e)
            }
        }

        private suspend fun handleGoogleSignIn(credential: Credential): Result {
            // Check if credential is of type Google ID
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                // Create Google ID Token
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                // Sign in to Firebase with using the token
                return firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
            } else {
                Log.w("GOOGLEOAUTH", "Credential is not of type Google ID!")
                return Result.Error(exception = Exception("Invalid credential"))
            }
        }
        // [END handle_sign_in]

        // [START auth_with_google]
        private suspend fun firebaseAuthWithGoogle(idToken: String): Result {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            return try {
                val result = auth.signInWithCredential(credential).await()
                Result.Success(result.user!!)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

        // Email/Password Authentication
        suspend fun loginWithEmailAndPassword(
            email: String,
            password: String,
        ): Result {
            return try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Result.Success(result.user!!)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

        suspend fun registerWithEmailAndPassword(
            email: String,
            password: String,
        ): Result {
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

