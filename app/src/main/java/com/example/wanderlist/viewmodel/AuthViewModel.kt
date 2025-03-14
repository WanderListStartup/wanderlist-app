package com.example.wanderlist.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    var uiState = mutableStateOf(LoginUIState())
        private set

    fun onEmailChange(e: String){
        uiState.value = uiState.value.copy(email = e)
    }
    fun onPasswordChange(p: String){
        uiState.value= uiState.value.copy(password = p)
    }

    init {
        checkAuth()
    }

    fun checkAuth(){
        if(auth.currentUser == null){
            _authState.value = AuthState.UnAuthenticated
        }
        else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(){
        _authState.value = AuthState.Loading

        if(uiState.value.email.isEmpty() || uiState.value.password.isEmpty()){
            _authState.value = AuthState.Error("Email and/or Password fields cannot be empty!")
            return
        }

            val email = uiState.value.email
            val password = uiState.value.password
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task->
                    if(task.isSuccessful) {
                        _authState.value = AuthState.Authenticated
                    } else {
                        _authState.value = AuthState.Error(task.exception?.message?:"Authorization Failed")
                    }

                }


    }

    fun register(){
        _authState.value = AuthState.Loading

        val email = uiState.value.email
        val password = uiState.value.password

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and/or Password fields cannot be empty!")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task->
                if(task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    Log.d("ngas","ngas")
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    _authState.value = AuthState.Error(task.exception?.message?:"Authorization Failed")
                }

            }
    }

    fun logout(){
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }

}

sealed class AuthState{
    data object Authenticated : AuthState()
    data object UnAuthenticated : AuthState()
    data object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}

data class LoginUIState(
   val email: String = "",
   val password: String = "",
)
