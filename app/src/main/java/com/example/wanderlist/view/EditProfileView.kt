package com.example.wanderlist.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wanderlist.components.BackCircle
import com.example.wanderlist.components.EditProfileTextField
import com.example.wanderlist.components.LoginTitle
import com.example.wanderlist.components.ProfilePictureCircle
import com.example.wanderlist.components.SectionTitle
import com.example.wanderlist.data.auth.model.AuthDataStore
import com.example.wanderlist.ui.theme.wanderlistBlue
import com.example.wanderlist.viewmodel.EditProfileViewModel
import com.example.wanderlist.viewmodel.AuthViewModel

@Composable
fun EditProfileView(
    onNavigateToProfile: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel(),
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with Back button and Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable {
                        onNavigateToProfile()
                    }
            ) {
                BackCircle()
            }

            Box(modifier = Modifier.align(Alignment.Center)) {
                LoginTitle("Edit Profile")
            }
        }

        // Profile picture display
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
        Spacer(modifier = Modifier.height(28.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            SectionTitle("About You")
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

            // Save button calls AuthViewModel to update Firestore
            Button(
                onClick = {
                    authViewModel.updateUserProfileSettings(
                        name = viewModel.name,
                        username = viewModel.username,
                        bio = viewModel.bio,
                        location = viewModel.location,
                        gender = viewModel.gender
                    ) { result ->
                        when (result) {
                            is AuthDataStore.Result.Success -> {
                                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            }
                            is AuthDataStore.Result.Error -> {
                                Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = wanderlistBlue),
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .align(Alignment.End)
                    .height(48.dp)
            ) {
                Text("Save", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
