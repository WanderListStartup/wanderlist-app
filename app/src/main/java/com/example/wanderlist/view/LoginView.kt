package com.example.wanderlist.view

import android.preference.PreferenceActivity.Header
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wanderlist.R
import com.example.wanderlist.components.HeaderLoginPage
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import com.example.wanderlist.components.SubHeaderLoginPage
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.graphics.Color


@Composable
fun LoginView(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.padding(top = 50.dp))
        Image(
            painter = painterResource(R.drawable.back_action_button),
            contentDescription = null,

//            modifier = Modifier.clickable { }
//                .padding(horizontal = 31.dp)

        )

        Spacer(modifier = Modifier.height(16.dp)) // Optional spacing for better alignment

        Column(modifier = Modifier.padding(start = 31.dp)) {
            HeaderLoginPage("Login and Start")
            HeaderLoginPage("Exploring! ")
            Spacer(modifier = Modifier.height(10.dp)) // Optional spacing for better alignment
            SubHeaderLoginPage("Don't have an account? ")
//            Row(Modifier.clickable { navController.navigate(Screens.createNewAccount) }) {
//                SubHeaderLoginPage("Click Here to SignUp", Color.Blue)
//            }
        }

    }



}