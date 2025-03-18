package com.example.wanderlist.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.model.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authDataStore: AuthDataStore
) : ViewModel() {


    private val _authState = MutableStateFlow<AuthState>(AuthState.UnAuthenticated)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkAuth()
    }


    fun checkAuth() {
        val user = authDataStore.getCurrentUser()
        _authState.value = if (user == null) AuthState.UnAuthenticated else AuthState.Authenticated
    }

    fun loginWithEmailAndPassword(email:String, password:String, callback: (result:AuthDataStore.Result)->Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authDataStore.loginWithEmailAndPassword(email, password)
            _authState.value = when (result) {
                is AuthDataStore.Result.Success -> AuthState.Authenticated
                is AuthDataStore.Result.Error -> AuthState.Error(result.exception.message ?: "Login failed")
                else -> AuthState.UnAuthenticated
            }
            callback(result)
        }
    }

    fun registerWithEmailAndPassword(email:String, password:String,callback: (result:AuthDataStore.Result)->Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authDataStore.registerWithEmailAndPassword(email, password)
            _authState.value = when (result) {
                is AuthDataStore.Result.Success -> AuthState.Authenticated
                is AuthDataStore.Result.Error -> AuthState.Error(result.exception.message ?: "Registration failed")
                else -> AuthState.UnAuthenticated
            }
            callback(result)
        }
    }

    fun handleGoogleOAuth() {
        val clientID = authDataStore.getGoogleOAuthClientID()
        authDataStore.googleOAuth()
    }

    fun logout() {
        authDataStore.logout()
        _authState.value = AuthState.UnAuthenticated
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object UnAuthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
