package com.example.wanderlist.data.fcm

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.wanderlist.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirebaseCloudMessageService : FirebaseMessagingService() {

    private val TAG = "FirebaseCloudMessageService"
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: new token ${token}")
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        if (userid != null){
            Firebase.firestore.collection("user_profiles")
                .document(userid)
                .update("fcmToken", token)
        }

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title
        val messageBody = message.notification?.body
        val notificationBuilder = NotificationCompat.Builder(this, "friend_request_receive")
            .setSmallIcon(R.drawable.wlist_logo)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this).notify(Random.nextInt(), notificationBuilder.build())
        } else {
            Log.w("Notifications", "Permission not granted: POST_NOTIFICATIONS")
        }
    }
}