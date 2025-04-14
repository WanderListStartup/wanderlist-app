package com.example.wanderlist.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wanderlist.viewmodel.PostReviewState
import com.example.wanderlist.viewmodel.WriteReviewViewModel

@Composable
fun WriteReviewView(
    establishmentId: String,
    onBack: () -> Unit,
    viewModel: WriteReviewViewModel = hiltViewModel()
) {
    // Read states from the viewmodel
    val rating = viewModel.userRating
    val reviewText = viewModel.reviewText
    val postState = viewModel.postReviewState

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // Top Bar with Back Arrow and Title
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(1.dp, Color.Black, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }
            Text(
                text = "Write a Review",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Container for Star Rating and Text Field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Row for star rating and prompt text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            // Custom star composable (assumes you have SelectableStarNoGesture available)
                            SelectableStarNoGesture(
                                starIndex = index,
                                rating = rating,
                                onRatingChanged = { newRating -> viewModel.updateRating(newRating) },
                                starSize = 28.dp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                    Text(
                        text = "Select your rating",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Multiline TextField for review text
                TextField(
                    value = reviewText,
                    onValueChange = { viewModel.updateReviewText(it) },
                    label = { Text("Write your review...") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to post the review
        Button(
            onClick = { viewModel.postReview(establishmentId)
                      println("Posting review with ID: $establishmentId") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Post Review", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display posting status for feedback
        when (postState) {
            is PostReviewState.Posting -> Text(text = "Posting...", color = Color.Gray)
            is PostReviewState.Success -> Text(text = "Review posted successfully!", color = Color.Green)
            is PostReviewState.Error -> Text(text = "Error: ${postState.message}", color = Color.Red)
            else -> { /* Idle: No message displayed */ }
        }
    }


}

@Composable
fun SelectableStarNoGesture(
    starIndex: Int,
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    starSize: Dp = 28.dp
) {
    Box(modifier = Modifier.size(starSize)) {
        // Determine which star icon should be displayed based on the rating.
        val icon = when {
            rating >= starIndex + 1 -> Icons.Filled.Star
            rating >= starIndex + 0.5f -> Icons.AutoMirrored.Filled.StarHalf
            else -> Icons.Filled.StarBorder
        }

        Icon(
            imageVector = icon,
            contentDescription = "Star ${starIndex + 1}",
            tint = Color(0xFFFFC107), // Gold color for stars.
            modifier = Modifier.fillMaxSize()
        )

        // Overlay two halves that are clickable to support half-star increments.
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onRatingChanged(starIndex + 0.5f) }
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onRatingChanged(starIndex + 1f) }
            )
        }
    }
}

