package com.example.wanderlist.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// If you have custom fonts/colors (e.g., Montserrat, wanderlistBlue), import them from your theme
// import com.example.wanderlist.ui.theme.Montserrat
// import com.example.wanderlist.ui.theme.wanderlistBlue
// etc.

@Composable
fun SignUpScreen(modifier: Modifier = Modifier) {
    // Local states for each text field
    var name by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    // Example gradient background (pink-ish)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFFFEDCE0), // Light pink
                        Color(0xFFFCE4EC)  // Lighter pink/purple
                    )
                )
            )
            .padding(24.dp) // Outer padding
    ) {
        // Main column for the entire form
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Top Heading
            Text(
                text = "Welcome!",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                    // fontFamily = Montserrat, if desired
                ),
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Create your Account",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                    // fontFamily = Montserrat
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 4.dp)
            )

            // Subheading: "Already have an account? Login"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Already have an account? ",
                    style = TextStyle(fontSize = 14.sp)
                )
                Text(
                    text = "Login",
                    style = TextStyle(
                        color = Color.Blue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    // Could make this clickable if you eventually add navigation
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // -- Outlined Text Fields --
            // 1) Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 2) Date of Birth
            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it },
                label = { Text("Date of Birth") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 3) Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 4) Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 5) Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 6) City
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // -- Create New Account button --
            Button(
                onClick = { /* handle create account click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5) // or your wanderlistBlue
                )
            ) {
                Text(
                    text = "Create New Account",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -- OR divider --
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = "  or  ",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -- Sign Up with Google button --
            OutlinedButton(
                onClick = { /* handle google sign up click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White
                )
            ) {
                // If you have a Google icon resource, replace this with painterResource(...)
                Icon(
                    imageVector = Icons.Default.Android, // placeholder
                    contentDescription = "Google Icon",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign Up with Google",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}
