package com.example.wanderlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ShowMoreSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 28.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Black,
        modifier = Modifier.padding(top = 24.dp)
    )
}

@Composable
fun PhotoGridView(photoURIs: List<String>) {

    val totalPhotos = photoURIs.size
    // However many rows + 3 Column Grid
    val rows = (totalPhotos + 2) / 3

    Column(modifier = Modifier.fillMaxWidth()) {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // For each column index in the row
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    if (index < totalPhotos) {
                        AsyncImage(
                            model = photoURIs[index],
                            contentDescription = "Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(20.dp))
                        )
                    } else {
                        // Fill in extra space for incomplete rows.
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TopThreePhotos(photoURIs: List<String>) {

    val totalPhotos = photoURIs.size
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // For each column index in the row
        for (col in 0 until 3) {
            val index = col
            if (index < totalPhotos) {
                AsyncImage(
                    model = photoURIs[index],
                    contentDescription = "Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20.dp))
                )
            } else {
                // Fill in extra space for incomplete rows.
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}