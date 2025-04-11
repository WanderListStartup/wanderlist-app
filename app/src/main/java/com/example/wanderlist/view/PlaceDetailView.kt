package com.example.wanderlist.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.example.wanderlist.R

/**
 * SinglePageCloneView replicates a single page from your homepage's
 * place exploring layout. It is hardcoded to match your dimensions and spacing,
 * and it includes placeholders for the cover image, about section, thumbnails,
 * a quests section, a placeholder info section, and a "Write a Review" section.
 *
 * The review section uses a custom selectable star rating bar (with half‑star increments)
 * that does not use detectTapGestures.
 */
@Composable
fun SinglePageCloneView() {
    val scrollState = rememberScrollState()

    // Hardcoded placeholders (replace with real data when ready)
    val placeName = "Hardcoded Coffee Shop"
    val placeRating = 4.5f
    val placeDistance = 2.0
    val coverImageUrl = "https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG"
    val aboutText = "This is a hardcoded About section. Swap this out for real editorialSummary or info."
    val photoThumbnails = listOf(
        "https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG",
        "https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG",
        "https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG"
    )
    val quests = listOf(
        "Quest #1: Try the signature mocha latte",
        "Quest #2: Take a photo outside the shop",
        "Quest #3: Chat with the barista for a travel tip"
    )
    val placeholderText = "Some placeholder text for user reviews, location info, or anything else."

    // Top Logo Row (mimics your homepage's logo row)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = rememberAsyncImagePainter(R.drawable.wlist_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .width(80.dp)
                .height(40.dp)
                .padding(start = 10.dp, top = 10.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    }

    // Main content column (vertically scrollable)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 50.dp)
    ) {
        // 1) Top Row: Place Name, Rating, Distance
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Place Name
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = placeName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
            // Right: Rating, Spacer, Distance, Icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "⭐ $placeRating")
                Spacer(modifier = Modifier.width(170.dp))
                Text(
                    text = String.format("%.1f mi", placeDistance),
                    color = Color(0xFF176FF2)
                )
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.distance),
                    contentDescription = "Distance Icon",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2) Cover Image
        Box(
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .width(350.dp)
                .height(345.dp)
                .clip(RoundedCornerShape(32.dp))
        ) {
            Image(
                painter = rememberAsyncImagePainter(coverImageUrl),
                contentDescription = placeName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3) "About" Section
        Text(
            text = "About",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 30.dp, bottom = 4.dp)
        )
        Box(modifier = Modifier.padding(start = 45.dp, bottom = 8.dp, end = 45.dp)) {
            Text(
                text = aboutText,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }

        // 4) Thumbnails Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            photoThumbnails.forEach { thumbUrl ->
                Image(
                    painter = rememberAsyncImagePainter(thumbUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .width(110.dp)
                        .height(95.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5) "Quests" Placeholder
        Text(
            text = "Quests",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 20.dp)
        )
        Column(modifier = Modifier.padding(start = 40.dp, top = 4.dp)) {
            quests.forEach { quest ->
                Text(
                    text = "• $quest",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 6) "Placeholder Section" for extra info
        Text(
            text = "Placeholder Section",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 20.dp, bottom = 4.dp)
        )
        Box(modifier = Modifier.padding(start = 40.dp)) {
            Text(
                text = placeholderText,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


    }
}

/**
 * A selectable star rating bar that does not use detectTapGestures.
 * This composable displays five stars and allows half‑star increments
 * by overlaying each star with two clickable halves.
 */

@Preview(showBackground = true)
@Composable
fun SinglePageCloneViewPreview() {
    SinglePageCloneView()
}
