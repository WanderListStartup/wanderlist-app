package com.example.wanderlist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.wanderlist.components.TopThreePhotos
import com.example.wanderlist.ui.theme.wanderlistBlue
import com.example.wanderlist.viewmodel.AuthViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.example.wanderlist.data.firestore.model.Category
import com.example.wanderlist.data.firestore.model.EstablishmentDetails
import com.example.wanderlist.viewmodel.HomePageViewModel
import kotlinx.coroutines.launch


@Composable
fun HomePageView(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    onNavigateToShowMore: (String) -> Unit,
    homePageViewModel: HomePageViewModel = hiltViewModel(),
    onNavigateToProfile: () -> Unit

) {

    val places = homePageViewModel.establishments.collectAsState().value
    val loading = homePageViewModel.isLoading.collectAsState().value
    MaterialTheme {

            HomeScreen(
                places = places,
                loading = loading,
                onNavigateToProfile = { onNavigateToProfile() },
                onNavigateToShowMore = { establishmentId ->
                    onNavigateToShowMore(establishmentId)
                },
            )
    }
}

@Composable
fun HomeScreen(
    places: List<EstablishmentDetails>,
    loading: Boolean,
    onNavigateToProfile: () -> Unit,
    onNavigateToShowMore: (String) -> Unit,
) {
    Scaffold(
        topBar = { TopBarCategories() },
        bottomBar = { BottomNavigationBar(onNavigateToProfile = onNavigateToProfile) },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if(loading || places.isEmpty()){
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            // Horizontal scroll for multiple places
            else {
                places.firstOrNull()?.let { currentPlace ->
                    key(currentPlace.id) {
                        PlaceContent(
                            place = currentPlace,
                            onNavigateToShowMore = onNavigateToShowMore,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopBarCategories() {
    val placesViewModel: HomePageViewModel = hiltViewModel()
    val selectedCategory by placesViewModel.selectedCategory.collectAsState()
//    var selectedCategory = remember { mutableStateOf("Food") }

    // List of category labels
    val categories = listOf(Category.FOOD, Category.BARS, Category.ENTERTAINMENT)

    Surface(
        color = Color.White,
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
        ) {
            // Logo row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.wlist_logo),
                    contentDescription = "Logo",
                    modifier =
                        Modifier
                            .width(70.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Categories row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center, // Center them horizontally
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    categories.forEach { category ->
                        if (category == selectedCategory) {
                            // Selected category: Blue pill
                            Box(
                                modifier =
                                    Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFE8F0FE))
                                        .clickable { placesViewModel.setSelectedCategory(category) },
                            ) {
                                Text(
                                    text = category.displayName,
                                    color = Color(0xFF176FF2),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                )
                            }
                        } else {
                            // Non-selected category: Gray text
                            Text(
                                text = category.displayName,
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier =
                                    Modifier
                                        .clickable { placesViewModel.setSelectedCategory(category) }
                                        .padding(horizontal = 8.dp, vertical = 8.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(onNavigateToProfile: () -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(72.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            selected = true,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
                selectedIconColor = wanderlistBlue,
                unselectedIconColor = Color.Gray,
            ),
            onClick = {
                onNavigateToProfile()
            },
        )

        Spacer(modifier = Modifier.width(1.dp))

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            selected = false,
            onClick = {
                onNavigateToProfile()
            },
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * Composable that displays the about text plus "Show More", but
 * **only** the "Show More" portion is clickable.
 */
@Composable
fun AboutTextWithShowMore(
    text: String,
    maxLines: Int = 4,
    onShowMoreClick: () -> Unit,
) {
    // Build an annotated string with "Show More" as a clickable region
    val annotatedText =
        buildAnnotatedString {
            // The about text
            append(text.trimEnd())

            // Add a space or punctuation before "Show More"
            append(" ")

            // Mark "Show More" region with an annotation
            pushStringAnnotation(tag = "SHOW_MORE", annotation = "SHOW_MORE")
            withStyle(style = SpanStyle(color = Color.Blue)) {
                append("Show More")
            }
            pop() // End of clickable region
        }

    // Use ClickableText so only the "Show More" region is clickable
    Text(
        text = annotatedText,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier =
            Modifier.clickable {
                onShowMoreClick()
            },
    )
}

@Composable
fun PlaceContent(
    place: EstablishmentDetails,
    onNavigateToShowMore: (String) -> Unit,
) {

    val homePageViewModel: HomePageViewModel = hiltViewModel()
    val scrollState = rememberScrollState()

    val offsetX = remember { Animatable(0f) }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        scope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount)
                        }
                    },
                    onDragEnd = {
                        if (offsetX.value <= -300f) {
                            homePageViewModel.addDislikedEstablishment(place.id)
                            homePageViewModel.removeSwipedEstablishment()

                        } else if (offsetX.value >= 300f) {
                            homePageViewModel.addLikedEstablishment(place.id)
                            homePageViewModel.removeSwipedEstablishment()
                        }
                        scope.launch {
                            offsetX.animateTo(0f)
                        }
                    }
                )
            }
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 0.dp),
    ) {
        // Restaurant name, rating, distance
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display name takes up remaining space
            Text(
                text = place.displayName ?: "no display names",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )

            // Rating immediately after the name
            Text(
                text = "⭐ ${place.rating}",
                modifier = Modifier.padding(start = 8.dp)
            )

            // Location block: text + icon, aligned on the right
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = "${String.format("%.1f", place.distance)} mi",
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
                        if (place.photoURIs?.size!! > 0) place.photoURIs.get(
                            0
                        ) else ""
                    ),
                    contentDescription = place.displayName,
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
                text = place.editorialSummary ?: "",
                maxLines = 4,
                onShowMoreClick = {
                    onNavigateToShowMore(place.id)
                },
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopThreePhotos((place.photoURIs ?: "") as List<String>)
        }


            Spacer(modifier = Modifier.height(16.dp))

        }


        val heartAlpha = (offsetX.value / 300f).coerceIn(0f, 1f)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 32.dp)
                .size(48.dp)
                .graphicsLayer { alpha = heartAlpha }
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Heart",
                tint = Color.Red.copy(alpha = heartAlpha),
                modifier = Modifier.size(24.dp)
            )
        }

        val xAlpha = (-offsetX.value / 300f).coerceIn(0f, 1f)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 32.dp)
                .size(48.dp)
                .graphicsLayer { alpha = xAlpha }
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "X",
                tint = Color.Black.copy(alpha = xAlpha),
                modifier = Modifier.size(24.dp)
            )
        }
    }


}




//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    val mockPlaces = listOf(
//        EstablishmentDetails(
//            id = "ChIJfQn6654P3okRAlMf2xMrWaU",
//            displayName = "Rensselaer Polytechnic Institute",
//            distance = 0.3576893437173981,
//            rating = 4.4,
//            photoURIs = listOf(
//            ),
//            location = LatLng(
//                42.7297628,
//                -73.6788884
//            ),
//            nationalPhoneNumber = "(518) 276-6000",
//            openingHours = "LMAO",
//            websiteUri = "http://www.rpi.edu/",
//            formattedAddress = "NOTHING",
//            editorialSummary = "NOTHING"
//        ),
//        PlaceDetails(
//            id = "ChIJm0I2GKYP3okRmnb0byFKApY",
//            displayName = "Dinosaur Bar-B-Que",
//            distance = 0.642556136738271,
//            rating = null, // You can add if available
//            photoURIs = listOf(
//            ),
//            location = LatLng(
//                42.734568,
//                -73.689239
//            ),
//            nationalPhoneNumber = "(518) 308-0400",
//            websiteUri = null,
//            openingHours = "NOTHING",
//            formattedAddress = "NOTHING",
//            editorialSummary ="NOTHING" // None listed in snippet
//        )
//    )
//    MaterialTheme {
//        HomeScreen(
//            places = mockPlaces,
//            loading = false,
//            onNavigateToProfile = {},
//            onNavigateToShowMore = {}
//        )
//    }
//}
