package com.example.wanderlist.view


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wanderlist.components.BackCircle
import com.example.wanderlist.components.LoginTitle
import com.example.wanderlist.components.PhotoGridView
import com.example.wanderlist.components.ShowMoreSectionTitle
import com.example.wanderlist.viewmodel.ShowMoreViewModel


@Composable
fun ShowMoreView(
    establishmentId: String,
    viewModel: ShowMoreViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},

    ) {
    LaunchedEffect(establishmentId) {
        viewModel.loadEstablishmentDetails(establishmentId)

    }

    val uriHandler = LocalUriHandler.current
    val scrollState = rememberScrollState()
    val extractedHours = extractWeekdayHours(viewModel.openingHours)



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)

    ) {
        // Top bar with back button & title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    // actually make the button a circle but idk why the png disappears .size(36.dp)
                    .clip(CircleShape)
                    .clickable { onBackClick() },
            ) {
                BackCircle()
            }
            Spacer(modifier = Modifier.weight(1f))
            LoginTitle("More Information")
            Spacer(modifier = Modifier.weight(0.3f))
        }

        Spacer(modifier = Modifier.height(15.dp))
        HorizontalDivider()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            // Clickable Website Link
            ShowMoreSectionTitle("Website")


            if (viewModel.websiteUrl.isBlank())
            {
                Text("No website URL available")
            }

            else {
                Text(
                    text = viewModel.websiteUrl,
                    color = Color.Blue,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clickable {
                            // Open link in browser

                            uriHandler.openUri(viewModel.websiteUrl)
                        }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))


            // Address
            ShowMoreSectionTitle("Address/Contact")

            if(viewModel.formattedAddress.isBlank())
            { Text("No Address Available") }

            else {
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = viewModel.formattedAddress)
            }


            // Phone Number / Contact
            if(viewModel.nationalPhoneNumber.isBlank()) { Text("No Phone Number Available") }
            else{ Text(text = viewModel.nationalPhoneNumber) }

            Spacer(modifier = Modifier.height(15.dp))


            // Hours of Operation
            ShowMoreSectionTitle("Hours of Operations")

            if (extractedHours.isEmpty())
            {
                Text(text = "No concrete Hours of Operations given")
            }

            else {
                extractedHours.forEach { dayHours ->
                    Text(dayHours)
                }
            }


            Spacer(modifier = Modifier.height(15.dp))


            // Photos
            ShowMoreSectionTitle("More Photos")
            Spacer(modifier = Modifier.height(15.dp))

            if (viewModel.photoURIs.isEmpty())
            {
                Text(text = "No available photos at the moment")
            }
            else {
                PhotoGridView(viewModel.photoURIs)
            }

            Spacer(modifier = Modifier.height(60.dp))

        }
    }
}


/* This function is for extracting the Hours of Operation from
* the Database Field String that is super long for no reason. */
fun extractWeekdayHours(openingHours: String): List<String> {
    val tag = "weekdayText=["
    val startIdx = openingHours.indexOf(tag)
    var weekdayHours: List<String> = emptyList()

    if (startIdx != -1) {
        val adjustedStart = startIdx + tag.length
        val endIdx = openingHours.indexOf("]", adjustedStart)
        if (endIdx != -1) {
            val weekdayHoursText = openingHours.substring(adjustedStart, endIdx)
            weekdayHours = weekdayHoursText.split(", ").map { it.trim() }
        } else {
            Log.e("ExtractWeekdayHours", "Closing bracket not found in openingHours string.")
        }
    } else {
        Log.e("ExtractWeekdayHours", "'weekdayText=[' not found in openingHours string.")
    }
    return weekdayHours
}




