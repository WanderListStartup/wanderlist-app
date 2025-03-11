package com.example.wanderlist.view

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderlist.ui.theme.Acme
import com.example.wanderlist.ui.theme.Alef
import com.example.wanderlist.ui.theme.Montserrat
import com.example.wanderlist.ui.theme.wanderlistBlue
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.example.wanderlist.R

@Composable
fun Landing( modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.image_1)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Spacer(modifier = Modifier.weight(0.5f))
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.padding(bottom = 54.dp).size(300.dp)
        )
        Text("Wanderlist", style= TextStyle(
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 29.sp
        )
        )
        Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal= 27.dp).padding(top= 10.dp),
            style= TextStyle(
                fontFamily = Acme,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                lineHeight = 21.sp
            )
        )
        Spacer(modifier = Modifier.weight(0.5f))
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){
            Button(onClick = { onClick() },
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = wanderlistBlue)
            ) {
                Text("Login",
                    style= TextStyle(fontFamily = Alef,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        lineHeight = 21.sp
                    ))
            }
            TextButton(
                onClick = { onClick() }
            ) {
                Text("Create Account",
                    color = wanderlistBlue,
                    style = TextStyle(
                        fontFamily = Alef,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        lineHeight = 21.sp
                    )
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }}

fun onClick() {
    //noop
    TODO("Not yet implemented")
}
