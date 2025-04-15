package com.example.wanderlist.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.firestore.model.Quests
import com.example.wanderlist.data.firestore.model.Reviews
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.data.firestore.repository.BadgesRepository
import com.example.wanderlist.data.firestore.repository.EstablishmentDetailsRepository
import com.example.wanderlist.data.firestore.repository.QuestsRepository
import com.example.wanderlist.data.firestore.repository.ReviewsRepository
import com.example.wanderlist.data.firestore.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LikedPlaceViewModel @Inject constructor(
    private val establishmentDetailsRepo: EstablishmentDetailsRepository,
    private val questsRepository: QuestsRepository,
    private val userRepository: UserProfileRepository,
    private val reviewsRepository: ReviewsRepository,
    private val badgesRepository: BadgesRepository,
    private val auth: FirebaseAuth,
) : ViewModel() {
    var likedEstablishmentDetails by mutableStateOf<EstablishmentDetails?>(null)
        private set

    var questForEstablishment by mutableStateOf<List<Quests>>(emptyList())
        private set

    var userCompletedQuests by mutableStateOf<List<String>>(emptyList())
        private set

    var reviewsForEstablishment by mutableStateOf<List<Reviews>>(emptyList())
        private set



    fun loadLikedEstablishmentDetails(establishmentId: String) {
        viewModelScope.launch {
            likedEstablishmentDetails = establishmentDetailsRepo.getEstablishmentDetails(establishmentId)
        }
    }

    fun loadQuestDetails(establishmentId: String)
    {
        viewModelScope.launch {
            questForEstablishment = questsRepository.getAllQuests(establishmentId)
        }
    }

    fun loadReviews(establishmentId: String) {
        viewModelScope.launch {
            reviewsForEstablishment = reviewsRepository.getAllReviewsForEstablishment(establishmentId)
        }
    }

    fun completeQuests(questId: String)
    {
        viewModelScope.launch {
            auth.uid?.let { userRepository.addQuests(it, questId) }
            auth.uid?.let { userRepository.incrementLevelByPointTwo(it) }
            val badgeId = badgesRepository.getBadgeId(userRepository.getUserLevel(auth.uid!!).toInt())
            if (badgeId.isNotEmpty()) {
                auth.uid?.let { userRepository.addBadgeToUserProfile(it, badgeId) }
            }
            getQuests()
        }
    }

    fun getQuests()
    {
        viewModelScope.launch {
            userCompletedQuests = userRepository.getQuests(auth.uid!!)
        }
    }


    // Here is the new suspend function that returns the review author's name.
    suspend fun getReviewAuthorSuspend(uid: String): String? {
        return userRepository.getNamebyId(uid)
    }




}