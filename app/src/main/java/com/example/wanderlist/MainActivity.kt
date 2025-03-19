package com.example.wanderlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.wanderlist.ui.theme.WanderlistTheme
import com.example.wanderlist.view.AppView
import com.example.wanderlist.view.PlacesScreen
import com.example.wanderlist.view.SignUpView
import com.example.wanderlist.viewmodel.SignUpViewModel

import dagger.hilt.android.AndroidEntryPoint
import com.example.wanderlist.viewmodel.PlacesViewModel


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WanderlistTheme {
                PlacesScreen()
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WanderlistTheme {
        PlacesScreen()
    }

}