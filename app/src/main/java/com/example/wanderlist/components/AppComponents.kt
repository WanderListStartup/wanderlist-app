package com.example.wanderlist.components

import android.graphics.Paint.Align
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wanderlist.ui.theme.Alef
import com.example.wanderlist.ui.theme.Montserrat
import com.example.wanderlist.viewmodel.SignUpViewModel


/*
* This File is for Text Components. This drastically
* improves readability in UI page code, by abstracting text settings
* to this file. If you want a text component that's on a different page
* simply just call the function it uses with your desire text parameter.
*
* TLDR: improves readability and reusability
*/

// For Both the Login Text and Create Account Text
@Composable
fun LoginText(value: String){
    Text(text = value,
        style= TextStyle(fontFamily = Alef,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 21.sp
        )
    )
}

// To be used for the rest of the "Wanderlist" title text
@Composable
fun LoginTitle(value: String) {
    Text(text = value,
        style= TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 29.sp
        )
    )
}

// To be used for the Logo Font as indicated on Figma
@Composable
fun LoginW(value: String) {
    Text(text = value,
        style= TextStyle(
            fontFamily = Alef,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 29.sp
        )
    )
}

@Composable
fun HeaderLoginPage(value: String)
{
    Text(text = value,
        style = TextStyle(
            fontFamily = Alef,
            fontWeight = FontWeight.Bold,
            fontSize = 29.sp,

        ))
}

@Composable
fun SubHeaderLoginPage(value: String, color: Color = Color.Black)
{
    Text(text = value,
        style = TextStyle(
            fontFamily = Alef,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = color,
        ))
}



@Composable
fun EmailInput(value: String, infoViewModel: SignUpViewModel) {

    OutlinedTextField(
        value = infoViewModel.email,
        onValueChange = { infoViewModel.onEmailChange(it) },
        label = {
            Text(
                text = value,
                lineHeight = 45.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        },
        modifier = Modifier.fillMaxWidth(0.95f).padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun PasswordInput(value: String, infoViewModel: SignUpViewModel) {

    OutlinedTextField(
        value = infoViewModel.password,
        onValueChange = { infoViewModel.onPasswordChange(it) },
        label = {
            Text(
                text = value,
                lineHeight = 45.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        },
        modifier = Modifier.fillMaxWidth(0.95f).padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp)
    )
}




