package com.example.wanderlist.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview

/**
 * Example composable that displays:
 *  - A "Write a Review" title
 *  - A box with a border that encloses:
 *     -- A row of stars (left) + "Select your rating" (right)
 *     -- A multiline text field below that row
 *  - A "Post Review" button below the box
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteAReviewBox() {
    // Keeps track of the user’s rating (0–5, in half increments).
    val userRating = remember { mutableStateOf(0f) }
    // For demonstration, we also track the typed text. (Not persisted anywhere.)
    val reviewText = remember { mutableStateOf("") }

    // 2) A custom row that acts like your top bar




    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center // Centers the "Naughters" text
        ) {
            // Back arrow placed at the start (left) of the Box.
            IconButton(
                onClick = { /* TODO: handle back action */ },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                // Wrap the Icon in a Box with a circular outline (border).
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }

            // Centered text
            Text(
                text = "Naughters",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            )
        }



        // 1) Title (e.g. "Write a Review") – center it or left-align as you wish


        Spacer(modifier = Modifier.height(16.dp))

        // 2) Outlined box containing the star row + text field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp) // internal padding so star row & text field aren’t flush
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // a) Row for star rating (left) + "Select your rating" (right)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Star row (left)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            SelectableStarNoGesture(
                                starIndex = index,
                                rating = userRating.value,
                                onRatingChanged = { newVal -> userRating.value = newVal },
                                starSize = 28.dp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                    // "Select your rating" text (right)
                    Text(
                        text = "Select your rating",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // b) Multiline text field
                TextField(
                    value = reviewText.value,
                    onValueChange = { newText -> reviewText.value = newText },
                    label = { Text("Start your review...") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Blue,
                        unfocusedBorderColor = Color.Blue,
                        cursorColor = Color.Blue,
                        // You can adjust other colors (e.g., label, error colors) as needed:
                        focusedLabelColor = Color.Blue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3) "Post Review" button below the box
        Button(
            onClick = { /* Submit logic here, if needed */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Post Review", color = Color.White)
        }
    }
}

/**
 * A star composable that supports half‑star increments by overlaying
 * two clickable halves. No detectTapGestures needed.
 */
@Composable
fun SelectableStarNoGesture(
    starIndex: Int,
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    starSize: Dp = 28.dp
) {
    Box(modifier = Modifier.size(starSize)) {
        // Decide which icon to show
        val icon = when {
            rating >= starIndex + 1 -> Icons.Filled.Star
            rating >= starIndex + 0.5f -> Icons.Filled.StarHalf
            else -> Icons.Filled.StarBorder
        }

        // The star
        Icon(
            imageVector = icon,
            contentDescription = "Star ${starIndex + 1}",
            tint = Color(0xFFFFC107),
            modifier = Modifier.fillMaxSize()
        )

        // Overlays two clickable halves
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

@Preview(showBackground = true)
@Composable
fun WriteAReviewBoxPreview() {
    WriteAReviewBox()
}
