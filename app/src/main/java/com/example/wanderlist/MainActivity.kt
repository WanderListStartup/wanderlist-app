package com.example.wanderlist

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.wanderlist.ui.theme.WanderlistTheme
import com.example.wanderlist.view.AppView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNotificationChannel()
        enableEdgeToEdge()
        setContent {
            WanderlistTheme {
                AppView()
            }
        }
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "friend_request_receive", // ID used when sending notifications
                "Friend Request",     // User-visible name
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "A user has sent a friend request!"
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WanderlistTheme {
        AppView()
    }
}
