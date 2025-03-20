package com.example.wanderlist.viewmodel;

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    // State for private account toggle
    var isPrivateAccount by mutableStateOf(false)
        private set

    // State for notifications toggle
    var isNotificationsEnabled by mutableStateOf(true)
        private set

    // Function to update private account state
    fun onPrivateAccountChange(newValue: Boolean) {
        isPrivateAccount = newValue
    }

    // Function to update notifications state
    fun onNotificationsChange(newValue: Boolean) {
        isNotificationsEnabled = newValue
    }

}
