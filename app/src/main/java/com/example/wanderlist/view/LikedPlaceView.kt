package com.example.wanderlist.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.wanderlist.viewmodel.LikedPlaceViewModel
import com.example.wanderlist.viewmodel.ShowMoreViewModel

@Composable
fun LikedPlaceView(
    establishmentId: String,
    onBackClick: () -> Unit,
    onNavigateToShowMore: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    showMoreViewModel: ShowMoreViewModel = hiltViewModel(),
    likedPlaceViewModel: LikedPlaceViewModel = hiltViewModel(),

) {


    LaunchedEffect(establishmentId) {
        showMoreViewModel.loadEstablishmentDetails(establishmentId)
        likedPlaceViewModel.loadLikedEstablishmentDetails(establishmentId)
        likedPlaceViewModel.loadQuestDetails(establishmentId)
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
                    likedPlacesViewModel = likedPlaceViewModel

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
        }
    }
}


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
