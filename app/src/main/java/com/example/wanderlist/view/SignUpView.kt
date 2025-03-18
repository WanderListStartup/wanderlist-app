package com.example.wanderlist.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wanderlist.viewmodel.SignUpViewModel
import com.example.wanderlist.ui.theme.Alef
import com.example.wanderlist.ui.theme.WorkSans

@Composable
fun SignUpView(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(),
    onBack: () -> Unit = {}  // New parameter for back navigation
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)  // Background set to white
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Back button at the top left
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            // Heading Section
            Text(
                text = "Welcome!",
                style = TextStyle(
                    fontFamily = Alef,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Create your Account",
                style = TextStyle(
                    fontFamily = Alef,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 4.dp)
            )

            // Subheading with login option
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Already have an account? ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = WorkSans
                )
                Text(
                    text = "Login",
                    fontFamily = WorkSans,
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input Fields with adjusted width and spacing
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = {
                    Text(
                        "Name",
                        lineHeight = 35.sp,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(0.95f),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = viewModel.city,
                onValueChange = { viewModel.onCityChange(it) },
                label = {
                    Text(
                        "City",
                        lineHeight = 35.sp,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(0.95f),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = viewModel.dob,
                onValueChange = { viewModel.onDobChange(it) },
                label = {
                    Text(
                        "Date of Birth",
                        lineHeight = 35.sp,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(0.95f),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = {
                    Text(
                        "Email",
                        lineHeight = 35.sp,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(0.95f),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = {
                    Text(
                        "Password",
                        lineHeight = 35.sp,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(0.95f),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChange(it) },
                label = {
                    Text(
                        "Confirm Password",
                        lineHeight = 35.sp,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(0.95f),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Spacer(modifier = Modifier.height(10.dp))
            // "Create New Account" button
            Button(
                onClick = { viewModel.onSignUp() },
                modifier = Modifier
                    .fillMaxWidth(1.0f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
            ) {
                Text(
                    "Create New Account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Divider with "or"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                Text("  or  ", fontSize = 14.sp, color = Color.Gray)
                HorizontalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "Sign Up with Google" button (without icon)
            OutlinedButton(
                onClick = { /* handle Google sign-up here */ },
                modifier = Modifier
                    .fillMaxWidth(1.0f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Text(
                    "Sign Up with Google",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
