package com.example.wanderlist.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderlist.R
import com.example.wanderlist.ui.theme.Acme
import com.example.wanderlist.ui.theme.Alef
import com.example.wanderlist.ui.theme.Exo
import com.example.wanderlist.ui.theme.Fascinate
import com.example.wanderlist.ui.theme.Montserrat
import com.example.wanderlist.ui.theme.wanderlistBlue

// Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//    Landing(
//        modifier = Modifier.padding(innerPadding)
//    )
// }

@Composable
fun Landing(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
) {
    val image = painterResource(R.drawable.image_1)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.padding(bottom = 54.dp).size(300.dp),
        )

          CustomFontTextRow()
        Spacer(modifier = Modifier.weight(0.5f))


        Spacer(modifier = Modifier.weight(0.5f))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onNavigateToLogin,
                modifier =
                    Modifier
                        .height(70.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = wanderlistBlue),
            ) {
                Text(
                    "Login",
                    style =
                        TextStyle(
                            fontFamily = Alef,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            lineHeight = 21.sp,
                        ),
                )
            }
            TextButton(
                onClick = onNavigateToRegister,
            ) {
                Text(
                    "Create Account",
                    color = wanderlistBlue,
                    style =
                        TextStyle(
                            fontFamily = Alef,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            lineHeight = 21.sp,
                        ),
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

// @Composable
// fun Login(onBackClick: ()->Unit){
//    Scaffold(
//        topBar = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(
//                            imageVector = Icons.,
//                            contentDescription = "Back",
//                            tint = Color.Black
//                        )
//                    }
//            )
//        }
//    ) { paddingValues ->
//        // Content goes here
//        Text("Login Screen Content", modifier = Modifier.padding(paddingValues))
//    }}


@Composable
fun CustomFontTextRow() {
    // Replace these with your actual font families.
    val fontForW: FontFamily = Fascinate     // Font for "W"
    val exoFont: FontFamily = Exo       // Font for "anderlist"
    val textStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 37.sp
    )
    Row {
        Text(
            text = "W",
            style = textStyle.copy(fontFamily = fontForW)
        )
        Text(
            text = "anderlist",
            style = textStyle.copy(fontFamily = exoFont)
        )
    }
}