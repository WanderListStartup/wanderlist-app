package com.example.wanderlist.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
    var username by mutableStateOf("")
        private set
    var isDomainValidState by mutableStateOf(true)
        private set

    // Functions to update state
    fun onNameChange(newName: String) {
        name = newName
    }

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onDobChange(newDob: String) {
        dob = newDob
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
        updateDomainValidity()
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



    private fun updateDomainValidity() {
        viewModelScope.launch {
            isDomainValidState = checkDomainAsync(email)
        }
    }

    // Performs the domain lookup on the IO dispatcher.
    private suspend fun checkDomainAsync(email: String): Boolean = withContext(Dispatchers.IO) {
        val domain = email.substringAfter("@").trim()
        if (domain.isEmpty()) {
            return@withContext false
        }
        return@withContext try {
            InetAddress.getByName(domain)
            true
        } catch (e: Exception) {
            false
        }
    }


    /**
     * Checks if [dob] is in the "MM/dd/yyyy" format and represents a valid date.
     * Returns true if valid; false otherwise.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun isValidDob(): Boolean {
        if (dob.length != 10) return false

        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        return try {
            val date = LocalDate.parse(dob, formatter)
            if (date.isAfter(LocalDate.now())) false else true
        } catch (e: DateTimeParseException) {
            false
        }
    }

}

