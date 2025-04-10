package com.example.wanderlist.data.firestore.repository

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class QuestsRepository @Inject constructor (
    private val firestore: FirebaseFirestore
) {
}