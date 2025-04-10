package com.example.wanderlist
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.mutableStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.wanderlist.data.googlemaps.model.PlaceDetails
import com.example.wanderlist.ui.theme.wanderlistBlue
import com.example.wanderlist.viewmodel.AuthViewModel
import com.example.wanderlist.viewmodel.PlacesViewModel
import com.example.wanderlist.viewmodel.EstablishmentIdHoldViewModel


@Composable
fun HomePageView(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    placesViewModel: PlacesViewModel = viewModel(),
    onNavigateToProfile: () -> Unit,
    onNavigateToShowMore: (String) -> Unit,

) {

    val places = placesViewModel.places.collectAsState().value
    MaterialTheme {
        // Example places with actual direct image URLs
//        val places = listOf(
//            Place(
//                name = "Naughters",
//                rating = 3.2,
//                distance = "0.3 mi",
//                coverImageUrl = "https://i0.wp.com/www.troyrecord.com/wp-content/uploads/2022/02/DSC_5566.jpg?fit=620,9999px&ssl=1&tbnid=NVplyHZxBYgiaM&vet=1",
//                aboutText = "Naughter's in Troy, New York is a diner and coffee shop ...",
//                thumbnailUrls = listOf(
//                    "https://lh3.googleusercontent.com/gps-cs-s/AB5caB-vaLr7eVC5t8VNIa79Jo4DK4wA4_7ki93JfMFFEbdmyeYNrPBHJX99IEokroqFu_dmCMI-QDJqW_WNJJcnSB9wwqKH_Y0saD3-9vSssDfK--d5b4bUrMZgGSr3tPqm_U_xM3pm=s1360-w1360-h1020",
//                    "https://lh3.googleusercontent.com/p/AF1QipO7FgCa3IkHZjpWSE8erilXu8TQYCIkrXeaFK2W=s1360-w1360-h1020",
//                    "https://lh3.googleusercontent.com/gps-cs-s/AB5caB8oMzpr4pLQB7hqTCO0dT53PhsTjAkTvjCyq34wTFvdLGvETNPv6_aBff7Mtp0-7vlFQH2K4ZFI0E_6AEZQ6QMKIW3xfxJ1YMdvWxD0zIzNs1orHnsw_Kwn906XBXoAIFzQYZ5T=s1360-w1360-h1020"
//                )
//            ),
//            Place(
//                name = "Starbucks",
//                rating = 4.1,
//                distance = "0.5 mi",
//                coverImageUrl = "https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG",
//                aboutText = "Famous coffeehouse chain offering specialty coffees, teas, & light bites...",
//                thumbnailUrls = listOf(
//                    "https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG",
//                    "https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG"
//                )
//            )
//        )
        HomeScreen(
            places = places,
            onNavigateToProfile = { onNavigateToProfile() },
            onNavigateToShowMore = { establishmentId ->
                onNavigateToShowMore(establishmentId)
            },
        )
    }
}

@Composable
fun HomeScreen(
    places: List<PlaceDetails>,
    onNavigateToProfile: () -> Unit,
    onNavigateToShowMore: (String) -> Unit,
) {
    val state = rememberLazyListState()
    Scaffold(
        topBar = { TopBarCategories() },
        bottomBar = { BottomNavigationBar(onNavigateToProfile = onNavigateToProfile) },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            // Horizontal scroll for multiple places
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                state = state,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
            ) {
                items(places) { place ->
                    // Each place item takes the full screen width
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        PlaceContent(
                            place,
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
    // Keep track of which category is selected
    var selectedCategory = remember { mutableStateOf("Food") }

    // List of category labels
    val categories = listOf("Food", "Bars", "Adventures", "Parks", "Activities")

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
                        if (category == selectedCategory.value) {
                            // Selected category: Blue pill
                            Box(
                                modifier =
                                    Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFE8F0FE))
                                        .clickable { selectedCategory.value = category },
                            ) {
                                Text(
                                    text = category,
                                    color = Color(0xFF176FF2),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                )
                            }
                        } else {
                            // Non-selected category: Gray text
                            Text(
                                text = category,
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier =
                                    Modifier
                                        .clickable { selectedCategory.value = category }
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
    place: PlaceDetails,
    onNavigateToShowMore: (String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 0.dp),
    ) {
        // Restaurant name, rating, distance
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = place.displayName ?: "no display names",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "⭐ ${place.rating}")
                Spacer(modifier = Modifier.width(170.dp))
                Text(
                    text = "${String.format("%.1f", place.distance)} mi",
                    color = Color(0xFF176FF2),
                )
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.distance),
                    contentDescription = "Distance Icon",
                    modifier =
                        Modifier
                            .width(40.dp)
                            .height(40.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        // Cover image
        Box(
            modifier =
                Modifier
                    .padding(horizontal = 30.dp)
                    .width(350.dp)
                    .height(345.dp)
                    .clip(RoundedCornerShape(32.dp)),
        ) {
            Image(
                painter = rememberAsyncImagePainter(if (place.photoURIs?.size!! > 0) place.photoURIs.get(0) else ""),
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
                text = place.editorialSummary ?: place.id,
                maxLines = 4,
                onShowMoreClick = {

                    onNavigateToShowMore(place.id)
                },
            )
        }

        // Thumbnails
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            place.photoURIs?.forEach { thumbUrl ->
                Image(
                    painter = rememberAsyncImagePainter(thumbUrl),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .width(110.dp)
                            .height(95.dp)
                            .clip(RoundedCornerShape(15.dp)),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        // Additional details for "Naughters" (example data)
        if (place.displayName == "Naughters") {
            AdditionalDetailsSection()
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AdditionalDetailsSection() {
    Column(
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
    ) {
        Text("Menu", style = MaterialTheme.typography.titleLarge)
        Text("http://naughters.com", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Address/Contact", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "136 2nd St, Troy, NY 12180\n(518) 238-3130",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text("Hour of Operations", style = MaterialTheme.typography.titleLarge)
        Text(
            text =
                """
                Monday 6AM–3PM
                Tuesday 6AM–3PM
                Wednesday 6AM–3PM
                Thursday 6AM–3PM
                Friday 6AM–3PM
                Saturday 8AM–5PM
                Sunday 8AM–5PM
                """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text("Accessibility", style = MaterialTheme.typography.titleLarge)
        Text("Wheelchair Accessible", style = MaterialTheme.typography.bodyMedium)
    }
}
