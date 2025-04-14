// SignUpView.kt
package com.example.wanderlist.view
import android.os.Build
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wanderlist.data.auth.model.AuthDataStore
import com.example.wanderlist.ui.theme.Alef
import com.example.wanderlist.ui.theme.WorkSans
import com.example.wanderlist.viewmodel.AuthViewModel
import com.example.wanderlist.viewmodel.SignUpViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpView(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel: SignUpViewModel = viewModel()
    Box(
        modifier =
        modifier
            .fillMaxSize()
            .background(Color.White) // Background set to white
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back",
                )
            }
            Text(
                text = "Welcome!",
                style =
                TextStyle(
                    fontFamily = Alef,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.align(Alignment.Start),
            )
            Text(
                text = "Create your Account",
                style =
                TextStyle(
                    fontFamily = Alef,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                modifier =
                Modifier
                    .align(Alignment.Start)
                    .padding(top = 4.dp),
            )
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = "Already have an account? ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = WorkSans,
                )
                Text(
                    modifier = Modifier.clickable { onNavigateToLogin() },
                    text = "Login",
                    fontFamily = WorkSans,
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = viewModel.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    label = {
                        Text(
                            "Name",
                            lineHeight = 35.sp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(12.dp),
                )
                Spacer(modifier = Modifier.height(10.dp))
                // The "username" field from the front end.
                OutlinedTextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.onUsernameChange(it) },
                    label = {
                        Text(
                            "Username",
                            lineHeight = 35.sp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(12.dp),
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = viewModel.dob,
                    onValueChange = { viewModel.onDobChange(it) },
                    label = {
                        Text(
                            "Date of Birth",
                            lineHeight = 35.sp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(12.dp),
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
                            fontSize = 15.sp,
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(12.dp),
                )
                Spacer(modifier = Modifier.height(10.dp))
                var passwordVisible by remember { mutableStateOf(false) }
                var confirmPasswordVisible by remember { mutableStateOf(false) }
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
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            val description = if (passwordVisible) "Hide password" else "Show password"
                            Icon(imageVector = icon, contentDescription = description)
                        }
                    }
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
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            val icon = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            val description = if (confirmPasswordVisible) "Hide password" else "Show password"
                            Icon(imageVector = icon, contentDescription = description)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    enabled = viewModel.isDomainValidState && viewModel.isValidDob(),
                    onClick = {
                        // Pass the city field as the backend location.
                        authViewModel.registerWithEmailAndPasswordAndProfile(
                            name = viewModel.name,
                            username = viewModel.username,
                            bio = "",
                            location = "Troy, NY",
                            gender = "",
                            dob = viewModel.dob,
                            email = viewModel.email,
                            password = viewModel.password,
                            phone = "",
                            isPrivateAccount = true,
                            isNotificationsEnabled = true
                        ) { result ->
                            when (result) {
                                is AuthDataStore.Result.Success -> onNavigateToHome()
                                is AuthDataStore.Result.Error ->
                                    Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier =
                    Modifier
                        .fillMaxWidth(1.0f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                ) {
                    Text(
                        "Create New Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    HorizontalDivider(
                        color = Color.Gray,
                        modifier =
                        Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                    )
                    Text("  or  ", fontSize = 14.sp, color = Color.Gray)
                    HorizontalDivider(
                        color = Color.Gray,
                        modifier =
                        Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {
                        authViewModel.googleOAuth { result ->
                            when (result) {
                                is AuthDataStore.Result.Success -> onNavigateToHome()
                                // need to implement an error page
                                is AuthDataStore.Result.Error -> {
                                    Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    modifier =
                    Modifier
                        .fillMaxWidth(1.0f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                ) {
                    Text(
                        "Sign Up with Google",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

    }
}