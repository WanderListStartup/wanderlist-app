package com.example.wanderlist.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderlist.components.BackCircle
import com.example.wanderlist.components.HeaderLoginPage
import com.example.wanderlist.components.LoginTitle
import com.example.wanderlist.components.SectionTitle
import com.example.wanderlist.components.ShowMoreSectionTitle
import com.example.wanderlist.components.SubHeaderLoginPage


@Composable
fun ShowMoreView(
    //onNavigateToLogin: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top bar with back button & title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    // actually make the button a circle but idk why the png disappears .size(36.dp)
                    .clip(CircleShape)
                    .clickable { /* navigation to Main Place Card onNavigateToLogin()  */ },
            ) {
                BackCircle()
            }
            Spacer(modifier = Modifier.weight(1f))
            LoginTitle("More Information")
            Spacer(modifier = Modifier.weight(0.3f))
        }

        Spacer(modifier = Modifier.height(15.dp))
        HorizontalDivider()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            ShowMoreSectionTitle("Website")

            Spacer(modifier = Modifier.height(15.dp))

            Text("buffer text / / website link ")

            Spacer(modifier = Modifier.height(15.dp))

            ShowMoreSectionTitle("Address/Contact")
            Spacer(modifier = Modifier.height(15.dp))

            Text("buffer text / / Address Line")
            Text("Phone # / / buffer text")

            Spacer(modifier = Modifier.height(15.dp))

            ShowMoreSectionTitle("Hours of Operations")
            Spacer(modifier = Modifier.height(15.dp))

            Text("buffer text / / Monday")
            Text("buffer text / / Tuesday")
            Text("buffer text / / Wednesday")
            Text("buffer text / / Thursday")
            Text("buffer text / / Friday")

            Spacer(modifier = Modifier.height(15.dp))

            ShowMoreSectionTitle("More Photos")

        }
    }
}