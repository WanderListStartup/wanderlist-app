package com.example.wanderlist.viewmodel

import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.auth.model.AuthDataStore
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.data.firestore.repository.UserProfileRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.UnAuthenticated)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkAuth()
    }

    fun registerWithEmailAndPasswordAndProfile(
        name: String,
        username: String,
        bio: String,
        location: String,
        gender: String,
        dob: String,
        email: String,
        password: String,
        phone: String,
        isPrivateAccount: Boolean,
        isNotificationsEnabled: Boolean,
        callback: (result: AuthDataStore.Result) -> Unit
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authDataStore.registerWithEmailAndPassword(email, password)
            if (result is AuthDataStore.Result.Success) {
                val user = authDataStore.getCurrentUser()
                if (user != null) {
                    try {
                        val profile = UserProfile(
                            uid = user.uid,
                            name = name,
                            username = username,
                            bio = bio,
                            location = "Troy, NY",
                            gender = gender,
                            dob = dob,
                            email = email,
                            phone = phone,
                            isPrivateAccount = isPrivateAccount,
                            isNotificationsEnabled = isNotificationsEnabled,
                            likedEstablishments = emptyList(),
                            reviews = emptyList(),
                            friends = emptyList(),
                            incomingRequests = emptyList(),
                            level = 0
                        )
                        userProfileRepository.createUserProfile(profile)
                        _authState.value = AuthState.Authenticated
                    } catch (e: Exception) {
                        _authState.value = AuthState.Error(e.message ?: "Failed to create user profile")
                    }
                } else {
                    _authState.value = AuthState.Error("User UID is null")
                }
            } else if (result is AuthDataStore.Result.Error) {
                _authState.value = AuthState.Error(result.exception.message ?: "Registration failed")
            }
            callback(result)
        }
    }

    fun updateUserSettings(
        phone: String,
        email: String,
        isPrivateAccount: Boolean,
        isNotificationsEnabled: Boolean,
        callback: (result: AuthDataStore.Result) -> Unit
    ) {
        viewModelScope.launch {
            val currentUser = authDataStore.getCurrentUser()
            if (currentUser != null) {
                val updatedFields = mapOf(
                    "phone" to phone,
                    "email" to email,
                    "isPrivateAccount" to isPrivateAccount,
                    "isNotificationsEnabled" to isNotificationsEnabled
                )
                try {
                    userProfileRepository.updateUserProfile(currentUser.uid, updatedFields)
                    callback(AuthDataStore.Result.Success(currentUser))
                } catch (e: Exception) {
                    callback(AuthDataStore.Result.Error(e))
                }
            } else {
                callback(AuthDataStore.Result.Error(Exception("User not logged in")))
            }
        }
    }

    fun updateUserProfileSettings(
        name: String,
        username: String,
        bio: String,
        location: String,
        gender: String,
        callback: (result: AuthDataStore.Result) -> Unit
    ) {
        viewModelScope.launch {
            val currentUser = authDataStore.getCurrentUser()
            if (currentUser != null) {
                val updatedFields = mapOf(
                    "name" to name,
                    "username" to username,
                    "bio" to bio,
                    "location" to location,
                    "gender" to gender
                )
                try {
                    userProfileRepository.updateUserProfile(currentUser.uid, updatedFields)
                    callback(AuthDataStore.Result.Success(currentUser))
                } catch (e: Exception) {
                    callback(AuthDataStore.Result.Error(e))
                }
            } else {
                callback(AuthDataStore.Result.Error(Exception("User not logged in")))
            }
        }
    }


        fun checkAuth() {
            val user = authDataStore.getCurrentUser()
            _authState.value = if (user == null) AuthState.UnAuthenticated else AuthState.Authenticated
        }

        fun googleOAuth(callback: (result: AuthDataStore.Result) -> Unit) {
            val googleIdOption =
                GetGoogleIdOption.Builder()
                    .setServerClientId(authDataStore.getGoogleOAuthClientID())
                    .setFilterByAuthorizedAccounts(true)
                    .build()

            val request =
                GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
            viewModelScope.launch {
                _authState.value = AuthState.Loading
                val result = authDataStore.startGoogleOAuth(request)
                _authState.value =
                    if (result is AuthDataStore.Result.Success) AuthState.Authenticated
                    else if (result is AuthDataStore.Result.Error) AuthState.Error(result.exception.message ?: "Login failed")
                    else AuthState.UnAuthenticated
                callback(result)
            }
        }

        fun loginWithEmailAndPassword(
            email: String,
            password: String,
            callback: (result: AuthDataStore.Result) -> Unit,
        ) {
            viewModelScope.launch {
                _authState.value = AuthState.Loading
                val result = authDataStore.loginWithEmailAndPassword(email, password)
                _authState.value =
                    if (result is AuthDataStore.Result.Success) AuthState.Authenticated
                    else if (result is AuthDataStore.Result.Error) AuthState.Error(result.exception.message ?: "Login failed")
                    else AuthState.UnAuthenticated
                callback(result)
            }
        }

        fun registerWithEmailAndPassword(
            email: String,
            password: String,
            callback: (result: AuthDataStore.Result) -> Unit,
        ) {
            viewModelScope.launch {
                _authState.value = AuthState.Loading
                val result = authDataStore.registerWithEmailAndPassword(email, password)
                _authState.value =
                    if (result is AuthDataStore.Result.Success) AuthState.Authenticated
                    else if (result is AuthDataStore.Result.Error) AuthState.Error(result.exception.message ?: "Registration failed")
                    else AuthState.UnAuthenticated
                callback(result)
            }
        }

        fun googleOAUTH() {
        }

        fun logout() {
            authDataStore.logout()
            _authState.value = AuthState.UnAuthenticated
        }




        fun deleteAccount( callback: (result: AuthDataStore.Result) -> Unit) {
            viewModelScope.launch {
                val currentUser = authDataStore.getCurrentUser()
                if (currentUser != null) {
                    try {
                        userProfileRepository.deleteUserProfile(currentUser.uid)
                        _authState.value = AuthState.UnAuthenticated
                        callback(AuthDataStore.Result.Success(currentUser))
                    } catch (e: Exception) {
                        callback(AuthDataStore.Result.Error(e))
                    }
                } else {
                    callback(AuthDataStore.Result.Error(Exception("User not logged in")))
                }
            }
        }

    private fun UserProfileRepository.deleteUserProfile(uid: String) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("user_profiles")
            .document(uid)
            .delete()
    }


}

sealed class AuthState {
    object Authenticated : AuthState()

    object UnAuthenticated : AuthState()

    object Loading : AuthState()

    data class Error(val message: String) : AuthState()
}