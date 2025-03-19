package com.example.wanderlist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderlist.R
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

@Composable
fun ProfilePictureCircle(
    imagePainter: Int,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(120.dp)
            .shadow(
                elevation = 10.dp, // This approximates a blur radius of 10
                shape = CircleShape,
                clip = false
            )
            .clip(CircleShape)
            .background(Color.LightGray) // A fallback background in case the image has transparency
    ) {
        Image(
            painter = painterResource(imagePainter),
            contentDescription = "Profile Picture",
            modifier = Modifier.size(120.dp)
        )
    }
}

// For each text field in the Edit Profile Page
@Composable
fun EditProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Label on the left.
        Text(
            text = label,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        // The text field on the right.
        OutlinedTextField(
            value = value,
            textStyle = TextStyle(
                fontFamily = Montserrat,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            onValueChange = onValueChange,
            modifier = Modifier.weight(2f),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color(0xFFF2F2F2)
            )

        )
    }
}

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


@Composable
fun BackCircle()
{
    Box(
        modifier = Modifier.padding()
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
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Black,
        modifier = Modifier.padding(top = 24.dp)
    )
}

@Composable
fun SettingItem(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp))
        Text(text = value, fontSize = 16.sp, color = Color.Gray,
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp))
    }
}

@Composable
fun ToggleSettingItem(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterVertically))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }

}

@Composable
fun ClickableSettingItem(title: String, isDestructive: Boolean = false, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDestructive) Color.Red else Color.Black

        )
    }
}




