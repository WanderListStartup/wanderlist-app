package com.example.wanderlist.view

import android.preference.PreferenceActivity.Header
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import com.example.wanderlist.components.SubHeaderLoginPage
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wanderlist.components.EmailInput
import com.example.wanderlist.components.LoginText
import com.example.wanderlist.components.PasswordInput
import com.example.wanderlist.model.AuthDataStore
import com.example.wanderlist.ui.theme.Alef
import com.example.wanderlist.ui.theme.wanderlistBlue
import com.example.wanderlist.viewmodel.AuthViewModel
import com.example.wanderlist.viewmodel.SignUpViewModel


@Composable
fun LoginView(
    viewModel: SignUpViewModel = viewModel(),
    authViewModel: AuthViewModel,
    onNavigateToLanding: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        // Back Button
        Spacer(modifier = Modifier.padding(top = 50.dp))
        Box(
            modifier = Modifier.padding()
                .clickable { onNavigateToLanding() }
                .padding(horizontal = 31.dp)
                .size(24.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Black, CircleShape)

        ) {
            Image(
                painter = painterResource(R.drawable.chevron_back_arrow),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }


        Spacer(modifier = Modifier.height(16.dp)) // Optional spacing for better alignment

        // Header Text w/ Click here to SignUp Navigation
        Column(modifier = Modifier.padding(start = 31.dp)) {
            // Top Header + Back Button
            HeaderLoginPage("Login and Start")
            HeaderLoginPage("Exploring! ")

            Spacer(modifier = Modifier.height(10.dp)) // Optional spacing for better alignment

            SubHeaderLoginPage("Don't have an account? ")

            Text(text = "Click here to Signup",
                modifier = Modifier.clickable { onNavigateToRegister() },
                style = TextStyle(
                    fontFamily = Alef,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Blue
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Email / Password Input Boxes / Login Navigation
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {

            // Email and Password Inputs
            EmailInput("Email", viewModel )
            Spacer(modifier = Modifier.height(10.dp))
            PasswordInput("Password", viewModel )

            Spacer(modifier = Modifier.height(275.dp))
            Button(
                onClick = {
                    authViewModel.loginWithEmailAndPassword(viewModel.email, viewModel.password){ result ->
                        when(result){
                            is AuthDataStore.Result.Success -> onNavigateToHome()
                            // need to implement an error page
                            is AuthDataStore.Result.Error -> Unit
                        }

                } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 31.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
            ) {
                LoginText("Login")
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Divider with "or"
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 34.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                Text("   or   ", fontSize = 14.sp, color = Color.Gray)
                HorizontalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            // Sign up with Google Button
            OutlinedButton(
                onClick = { /* handle Google sign-up here */ },
                modifier = Modifier
                    .fillMaxWidth(1.0f)
                    .height(60.dp)
                    .padding(horizontal = 31.dp),
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