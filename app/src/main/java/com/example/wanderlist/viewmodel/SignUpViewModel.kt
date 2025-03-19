package com.example.wanderlist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    // State variables for each input field
    var name by mutableStateOf("")
        private set
    var dob by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    var city by mutableStateOf("")
        private set

    // Functions to update state
    fun onNameChange(newName: String) {
        name = newName
    }

    fun onDobChange(newDob: String) {
        dob = newDob
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
    }

    fun onCityChange(newCity: String) {
        city = newCity
    }

}
