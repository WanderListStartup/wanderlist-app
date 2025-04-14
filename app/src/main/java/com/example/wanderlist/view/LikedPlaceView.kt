package com.example.wanderlist.view

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.wanderlist.AboutTextWithShowMore
import com.example.wanderlist.R
import com.example.wanderlist.components.BackCircle
import androidx.compose.ui.unit.sp
import com.example.wanderlist.components.TopThreePhotos
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.firestore.model.Reviews
import com.example.wanderlist.viewmodel.LikedPlaceViewModel
import com.example.wanderlist.viewmodel.ShowMoreViewModel

@Composable
fun LikedPlaceView(
    establishmentId: String,
    onBackClick: () -> Unit,
    onNavigateToShowMore: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToWriteReview: (String) -> Unit,
    showMoreViewModel: ShowMoreViewModel = hiltViewModel(),
    likedPlaceViewModel: LikedPlaceViewModel = hiltViewModel(),

) {


    LaunchedEffect(establishmentId) {
        showMoreViewModel.loadEstablishmentDetails(establishmentId)
        likedPlaceViewModel.loadLikedEstablishmentDetails(establishmentId)
        likedPlaceViewModel.loadQuestDetails(establishmentId)
        likedPlaceViewModel.loadReviews(establishmentId)
        likedPlaceViewModel.getQuests()
    }

    Scaffold (
        topBar = { WlTopBar() },
        bottomBar = { BottomNavigationBar(onNavigateToHome = onNavigateToHome) },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            likedPlaceViewModel.likedEstablishmentDetails?.let {
                PlaceContent(
                    establishment = it,
                    onBackClick = onBackClick,
                    onNavigateToShowMore = { establishmentId ->
                        onNavigateToShowMore(establishmentId)
                    },
                    likedPlacesViewModel = likedPlaceViewModel,
                    onNavigateToWriteReview = onNavigateToWriteReview
                )
            }
        }
    }
}

@Composable
fun PlaceContent(
    establishment: EstablishmentDetails,
    onBackClick: () -> Unit,
    onNavigateToShowMore: (String) -> Unit,
    likedPlacesViewModel:  LikedPlaceViewModel = hiltViewModel(),
    onNavigateToWriteReview: (String) -> Unit,
) {
    val scrollState = rememberScrollState()


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(top = 0.dp),
        ) {
            // Top Row with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onBackClick() },
                ) {
                    BackCircle()
                }
                Spacer(modifier = Modifier.height(16.dp))
            }


            // Restaurant name, rating, distance
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display name takes up remaining space
                Text(
                    text = establishment.displayName ?: "no display names",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )

                // Rating immediately after the name
                Text(
                    text = "⭐ ${establishment.rating}",
                    modifier = Modifier.padding(start = 8.dp)
                )

                // Location block: text + icon, aligned on the right
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "${String.format("%.1f", establishment.distance)} mi",
                        color = Color(0xFF176FF2)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
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
            // Cover image
            Box(
                modifier =
                    Modifier
                        .padding(horizontal = 30.dp)
                        .height(345.dp)
                        .clip(RoundedCornerShape(32.dp)),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        if (establishment.photoURIs?.size!! > 0) establishment.photoURIs.get(
                            0
                        ) else ""
                    ),
                    contentDescription = establishment.displayName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            // About section
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "About",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 20.dp, bottom = 4.dp),
            )

            // Only "Show More" is clickable here
            Box(modifier = Modifier.padding(start = 40.dp, bottom = 8.dp)) {
                AboutTextWithShowMore(
                    text = establishment.editorialSummary ?: "",
                    maxLines = 4,
                    onShowMoreClick = {
                        establishment.let { onNavigateToShowMore(it.id) }
                    },
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TopThreePhotos((establishment.photoURIs ?: "") as List<String>)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quests section
            Text(
                text = "Quests",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 20.dp, bottom = 4.dp),
            )

            val quests = likedPlacesViewModel.questForEstablishment
            val completedQuests = likedPlacesViewModel.userCompletedQuests

            Column(modifier = Modifier.padding(start = 40.dp, top = 4.dp)) {
                quests.forEachIndexed { _, quest ->
                    QuestCheckBox(
                        title = quest.questName,
                        checked = quest.questId in completedQuests,
                        onCheckedChange = { likedPlacesViewModel.completeQuests(quest.questId) }
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Write a Review section
            Text(
                text = "Write A Review",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 20.dp, bottom = 4.dp),
            )
            Column(
                modifier = Modifier.padding(start = 20.dp, top = 4.dp, end = 20.dp)
            ) {
                WriteReviewCard { onNavigateToWriteReview(establishment.id)  }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List Reviews for Establishment
            Text(
                text = "User Reviews",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 20.dp, bottom = 4.dp),
            )

            val reviews = likedPlacesViewModel.reviewsForEstablishment

            if (reviews.isEmpty()) {
                // If there are no reviews, show a message.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Reviews Yet. Write a Review!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
            } else {
                Column {
                    // Only take up to 5 reviews from the list
                    reviews.take(5).forEach { review ->
                        ReviewItem(
                            review = review,
                            likedPlacesViewModel = likedPlacesViewModel
                        )
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))

            }

        }

    }
}

// Quest CheckBox.
@Composable
fun QuestCheckBox(
    title: String,
    checked: Boolean,
    maxLines: Int = 2,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 275.dp)
        )
    }
}

// Card to Write a Review for the Establishment
@Composable
fun WriteReviewCard(
    onClick: () -> Unit
) {
    Card(
        // Rounded corners & shadow
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            // Make the entire card clickable
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // Internal padding for spacing
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side text
            Text(
                text = "Write a Review",
                style = MaterialTheme.typography.titleMedium
            )
            // Edit/Write icon
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// Card to Hold each Review from a User
@Composable
fun ReviewItem(
    review: Reviews,
    likedPlacesViewModel: LikedPlaceViewModel = hiltViewModel()
) {
    // Get AuthorName
    val authorName by produceState(initialValue = "Loading...", key1 = review.userId) {
        value = likedPlacesViewModel.getReviewAuthorSuspend(review.userId) ?: "Unknown"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Author Name Top Left
                Text(
                    text = authorName,
                    style = MaterialTheme.typography.titleMedium
                )
                // Stars Corresponding to Rating in Top Right
                RatingBar(rating = review.rating)
            }


            Spacer(modifier = Modifier.height(8.dp))

            // The review text, accounting for overflow if super long review text
            Text(
                text = review.reviewText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}

// Simple Function for Outputting Rating as Gold Stars
// Out of a 5-star rating
@Composable
fun RatingBar(rating: Int) {
    // For a 5-star scale, we'll round the rating to an integer (0..5)
    val filledStars = rating
    val maxStars = 5

    Row(verticalAlignment = Alignment.CenterVertically) {
        // Draw filled stars
        repeat(filledStars) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star",
                tint = Color(0xFFFFD700), // gold color
                modifier = Modifier.size(20.dp)
            )
        }
        // Draw outlined stars for the remainder (if you want a 5-star display)
        repeat(maxStars - filledStars) {
            Icon(
                imageVector = Icons.Default.StarBorder,
                contentDescription = "Empty Star",
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

///**
// * A selectable star rating bar that does not use detectTapGestures.
// * This composable displays five stars and allows half‑star increments
// * by overlaying each star with two clickable halves.
// */
//
//@Preview(showBackground = true)
//@Composable
//fun SinglePageCloneViewPreview() {
//    LikedPlaceView()
//}



