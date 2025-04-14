package com.example.wanderlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.data.googlemaps.model.PlaceDetails
import com.example.wanderlist.view.RatingBar
import com.google.firebase.vertexai.type.content


@Composable
fun LikedPlacesGrid(
    places: List<EstablishmentDetails>,
    onNavigateToLikedPlace: (String) -> Unit,
    maxLines: Int = 1,

    ) {
    val columns = 2
    val rows = (places.size + columns - 1) / columns

    Column(modifier = Modifier.fillMaxWidth()) {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (col in 0 until columns) {
                    val index = row * columns + col
                    if (index < places.size) {
                        val place = places[index]
                        val firstPhoto = place.photoURIs?.firstOrNull()

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp)
                                .clickable {
                                    onNavigateToLikedPlace(place.id)
                                }
                        ) {
                            Column {
                                // If there's at least one photo, display the first one
                                if (!firstPhoto.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = firstPhoto,
                                        contentDescription = "Place image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                            .clickable {
                                                onNavigateToLikedPlace(place.id)
                                            }
                                    )
                                }

                                place.displayName?.let {
                                    Text(
                                        text = it,
                                        maxLines = maxLines,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {
                                                onNavigateToLikedPlace(place.id)
                                            }
                                    )
                                }
                            }
                        }
                    } else {
                        // If there's no place for this column, just use spacer to fill
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}


@Composable
fun ProfileReviewCard(
    establishmentName: String,
    location: String,
    rating: Int,
    reviewText: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp) // space between text column & edit icon
            ) {
                // Top row: Establishment name & location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = establishmentName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Review text, truncated to 2 lines
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reviewText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )

                // Star rating
                Spacer(modifier = Modifier.height(4.dp))
                RatingBar(rating = rating)
            }

            Row(
            ) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(36.dp)
                        .padding(0.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Review",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDeleteClick,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(0.dp),
                    ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Review",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}



