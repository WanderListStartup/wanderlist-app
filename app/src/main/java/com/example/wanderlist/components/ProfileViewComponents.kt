package com.example.wanderlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.wanderlist.data.googlemaps.model.PlaceDetails


@Composable
fun LikedPlacesGrid(
    places: List<PlaceDetails>,
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
                                    // add navigation //
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
                                                // add navigation //
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
                                                // add navigation //
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


