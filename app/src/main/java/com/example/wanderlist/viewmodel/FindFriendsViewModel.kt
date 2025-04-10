// FindFriendsViewModel.kt
package com.example.wanderlist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.data.firestore.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindFriendsViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    // Text in the search bar
    var searchQuery by mutableStateOf("Logan Pa")
        private set

    // All user profiles from Firestore
    var allProfiles by mutableStateOf<List<UserProfile>>(emptyList())
        private set

    // Initialization: fetch all user profiles
    init {
        viewModelScope.launch {
            allProfiles = userProfileRepository.getAllUserProfiles()
        }
    }

    // Called whenever the search text changes
    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    // Return the filtered profiles based on name or username
    fun filteredProfiles(): List<UserProfile> {
        val query = searchQuery.trim().lowercase()
        return allProfiles.filter { profile ->
            profile.name.lowercase().contains(query) ||
                    profile.username.lowercase().contains(query)
        }
    }
}
