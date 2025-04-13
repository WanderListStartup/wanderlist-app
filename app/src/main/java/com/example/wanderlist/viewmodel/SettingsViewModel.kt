// SettingsViewModel.kt
package com.example.wanderlist.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.firestore.repository.UserProfileRepository
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.data.auth.model.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val authDataStore: AuthDataStore,
) : ViewModel() {

    var phone by mutableStateOf("646-112-1323")
        private set

    var email by mutableStateOf("abd@gmail.com")
        private set

    var isPrivateAccount by mutableStateOf(false)
        private set

    var isNotificationsEnabled by mutableStateOf(userProfileRepository.checkNotificationPermission())
        private set

    var showingNotificationDialog by mutableStateOf(false)
        private set

    init {
        loadUserProfile()
    }

    fun checkNotificationPermissionStatus() : Boolean{
        return userProfileRepository.checkNotificationPermission()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val currentUser = authDataStore.getCurrentUser()
            if (currentUser != null) {
                val profile: UserProfile? = userProfileRepository.getUserProfile(currentUser.uid)
                if (profile != null) {
                    Log.d("SettingsViewModel", "Profile loaded: $profile")
                    phone = profile.phone
                    email = profile.email
                    isPrivateAccount = profile.isPrivateAccount
                    isNotificationsEnabled = profile.isNotificationsEnabled
                } else {
                    Log.e("SettingsViewModel", "User profile is null for UID: ${currentUser.uid}")
                }
            } else {
                Log.e("SettingsViewModel", "No user found, not logged in?")
            }
        }
    }


    fun onPhoneChange(newValue: String) {
        phone = newValue
    }

    fun onEmailChange(newValue: String) {
        email = newValue
    }

    fun onPrivateAccountChange(newValue: Boolean) {
        isPrivateAccount = newValue
    }

    fun onNotificationsChange(newValue: Boolean) {
        if (newValue){
            showingNotificationDialog = true
            isNotificationsEnabled = true
        } else{
           //noop cannot turn off notification perms inside of app
            return
        }
    }

    fun dismissNotificationDialog(){
       showingNotificationDialog = false
    }

}
