package com.example.wanderlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wanderlist.components.BackCircle
import com.example.wanderlist.ui.theme.wanderlistBlue
import com.example.wanderlist.components.EditProfileTextField
import com.example.wanderlist.components.LoginTitle
import com.example.wanderlist.components.ProfilePictureCircle
import com.example.wanderlist.components.SectionTitle
import com.example.wanderlist.ui.theme.Montserrat
import com.example.wanderlist.viewmodel.EditProfileViewModel

@Composable
fun EditProfileView(
    viewModel: EditProfileViewModel = viewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            // BackCircle aligned at the start.
            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                BackCircle()
            }

            // LoginTitle centered in the Box.
            Box(modifier = Modifier.align(Alignment.Center)) {
                LoginTitle("Edit Profile")
            }
        }

        // profile picture circle below the header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            ProfilePictureCircle(
                imagePainter = viewModel.profilePicture
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Change Profile Picture",
            fontSize = 14.sp,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            color = Color(0xFF196EEE)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            SectionTitle(
                "About You"
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))


            EditProfileTextField(
                label = "Name",
                value = viewModel.name,
                onValueChange = { viewModel.onNameChange(it) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))


            EditProfileTextField(
                label = "Username",
                value = viewModel.username,
                onValueChange = { viewModel.onUsernameChange(it) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))


            EditProfileTextField(
                label = "Bio",
                value = viewModel.bio,
                onValueChange = { viewModel.onBioChange(it) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))


            EditProfileTextField(
                label = "Location",
                value = viewModel.location,
                onValueChange = { viewModel.onLocationChange(it) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))


            EditProfileTextField(
                label = "Gender",
                value = viewModel.gender,
                onValueChange = { viewModel.onGenderChange(it) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(32.dp))

            // Save button at the bottom.
            Button(
                onClick = { viewModel.saveProfile() },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = wanderlistBlue),
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .align(Alignment.End)
                    .height(48.dp)
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

