package com.example.wanderlist.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
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


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // "Gender" label, same style as "Name" or "Location"
                Text(
                    text = "Gender",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.width(80.dp) // Or .weight(1f) if you want flexible sizing
                )

                // The dropdown on the right
                ForcedDownwardDropdown(
                    currentGender = viewModel.gender,
                    onGenderChange = { selected ->
                        viewModel.onGenderChange(selected)
                    },
                    modifier = Modifier.weight(1f)
                )
            }

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

@Composable
fun ForcedDownwardDropdown(
    currentGender: String,
    onGenderChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }
    val textFieldSize = remember { mutableStateOf(Size.Zero) }
    val genderOptions = listOf("Male", "Female", "Other")

    Box(modifier = modifier.padding(start = 10.dp)) {
        OutlinedTextField(
            value = currentGender,
            onValueChange = { /* No direct typing; use the dropdown */ },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded.value = !expanded.value }) {
                    Icon(
                        imageVector = if (expanded.value) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                // Make the outline transparent in all states
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            ),
            modifier = Modifier
                .width(130.dp)
                .onGloballyPositioned { coords ->
                    textFieldSize.value = coords.size.toSize()
                }
        )

        if (expanded.value) {
            Popup(
                alignment = Alignment.TopStart,
                offset = with(LocalDensity.current) {
                    IntOffset(x = 0, y = textFieldSize.value.height.toInt())
                },
                onDismissRequest = { expanded.value = false }
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.width(
                        with(LocalDensity.current) { textFieldSize.value.width.toDp() }
                    )
                ) {
                    Column {
                        genderOptions.forEach { gender ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onGenderChange(gender)
                                        expanded.value = false
                                    }
                                    .padding(12.dp)
                            ) {
                                Text(text = gender, fontWeight = FontWeight.Normal)
                            }
                        }
                    }
                }
            }
        }
    }
}
