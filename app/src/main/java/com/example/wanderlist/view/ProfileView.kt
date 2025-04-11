package com.example.wanderlist.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.wanderlist.R
import com.example.wanderlist.components.LikedPlacesGrid
import com.example.wanderlist.ui.theme.wanderlistBlue
import com.example.wanderlist.viewmodel.AuthViewModel
import com.example.wanderlist.viewmodel.EditProfileViewModel
import com.example.wanderlist.viewmodel.PlacesViewModel
import com.google.android.gms.maps.model.Circle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    viewModel: EditProfileViewModel = hiltViewModel(),
    authViewModel: AuthViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToUserSettings: () -> Unit,
    placesViewModel: PlacesViewModel = viewModel()
) {
    val selectedTab = remember { mutableIntStateOf(0)
    }
    val context = LocalContext.current
    val places = placesViewModel.places.collectAsState().value

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                navigationIcon = {
                    Image(
                        painter = rememberAsyncImagePainter(R.drawable.bigwlist),
                        contentDescription = "WList Logo",
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .width(66.dp)
                            .height(53.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(onNavigateToHome = onNavigateToHome)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        "https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG"
                    ),
                    contentDescription = "User Profile Picture",
                    modifier = Modifier
                        .size(125.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = viewModel.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        fontSize = 32.sp,
                        modifier = Modifier.padding(PaddingValues(start = 36.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = viewModel.username,
                        modifier = Modifier.padding(PaddingValues(start = 38.dp)),
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier
                            .padding(start = 40.dp, end = 40.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Followers", color = Color.Gray)
                            Text(text = "15", fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Following", color = Color.Gray)
                            Text(text = "20", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(PaddingValues(start = 30.dp, top = 5.dp))
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.distance),
                    contentDescription = "Distance Icon",
                    modifier = Modifier
                        .width(25.dp)
                        .height(20.dp)
                        .padding(PaddingValues(end = 0.dp)),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = viewModel.location,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(start = 28.dp, top = 8.dp, bottom = 8.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Level 10",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .width(250.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .border(width = 2.dp, color = Color.Black, shape = CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.4f)
                            .background(Color(0xFF176FF2))
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = viewModel.bio,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(PaddingValues(start = 15.dp, end = 30.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(start = 45.dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onNavigateToUserSettings() },
                    modifier = Modifier.shadow(elevation=6.dp, shape= RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Edit Profile", color = Color.Black)
                }
                IconButton(
                    onClick = { onNavigateToSettings() },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Black),
                ) {
                   Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                }
            }
            Row(modifier = Modifier.clip(CircleShape).size(36.dp).align(Alignment.CenterHorizontally)
                )
            {
                HorizontalDivider(modifier = Modifier.padding(top=15.dp))

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { selectedTab.intValue = 0 }
                ) {
                    Icon(Icons.Rounded.Favorite, contentDescription = "Tab 0")
                }
                IconButton(
                    onClick = { selectedTab.intValue = 1 }
                ) {
                    Icon(Icons.AutoMirrored.Rounded.Assignment, contentDescription = "Tab 1")
                }
                IconButton(
                    onClick = { selectedTab.intValue = 2 }
                ) {
                    Icon(Icons.Rounded.Group, contentDescription = "Tab 2")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 72.dp)
            ) {
                when (selectedTab.intValue) {
                    0 -> {
                        // replace Places with
                        // List<PlaceDetails> userLikedPlaces = [place, place2, ... ]
                        LikedPlacesGrid(places)
                    }
                    1 -> {
                        Text(
                            text = "Quest tab content here",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    2 -> {
                        Spacer(modifier = Modifier.height(15.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = { /* Add Friend logic */ },
                                shape = RoundedCornerShape(15.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFF176FF2)),
                                modifier = Modifier
                                    .width(275.dp)
                                    .height(40.dp)
                            ) {
                                Text(text = "Add Friend")
                            }
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(horizontal = 40.dp, vertical = 20.dp)
                                    .fillMaxWidth(),
                                thickness = 1.dp,
                                color = Color.Gray.copy(alpha = 0.2f)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(PaddingValues(top = 60.dp))
                        ) {
                            UserRow(
                                imageUrl = "https://example.com/logan_paul.jpg",
                                name = "Logan Paul",
                                handle = "@lPaul_45",
                                location = "Los Angeles, CA",
                                level = "Lvl: 2"
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(horizontal = 40.dp)
                                    .fillMaxWidth(),
                                thickness = 1.dp,
                                color = Color.Gray.copy(alpha = 0.2f)
                            )
                            UserRow(
                                imageUrl = "https://example.com/walter_white.jpg",
                                name = "Walter White",
                                handle = "@heisenberg",
                                location = "Albuquerque, NM",
                                level = "Lvl: 999"
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(horizontal = 40.dp)
                                    .fillMaxWidth(),
                                thickness = 1.dp,
                                color = Color.Gray.copy(alpha = 0.2f)
                            )
                            UserRow(
                                imageUrl = "https://example.com/angel_reese.jpg",
                                name = "Angel Reese",
                                handle = "@AngBricks",
                                location = "Chicago, IL",
                                level = "Lvl: 22"
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(horizontal = 40.dp)
                                    .fillMaxWidth(),
                                thickness = 1.dp,
                                color = Color.Gray.copy(alpha = 0.2f)
                            )
                            UserRow(
                                imageUrl = "https://example.com/lebron_james.jpg",
                                name = "LeBron James",
                                handle = "@LeWanderer",
                                location = "Los Angeles, CA",
                                level = "Lvl: 999"
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(horizontal = 40.dp)
                                    .fillMaxWidth(),
                                thickness = 1.dp,
                                color = Color.Gray.copy(alpha = 0.2f)
                            )
                            // ... additional rows as needed
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(onNavigateToHome: () -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(72.dp),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            selected = false,
            onClick = {
                onNavigateToHome()
            },
        )

        Spacer(modifier = Modifier.width(1.dp))

        NavigationBarItem(
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
                selectedIconColor = wanderlistBlue,
                unselectedIconColor = Color.Gray,
            ),
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            selected = true,
            onClick = {
            },
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun UserRow(
    imageUrl: String,
    name: String,
    handle: String,
    location: String,
    level: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(30.dp))
        Image(
            painter = rememberAsyncImagePainter(
                // Hard-coded same image for demonstration
                "https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG"
            ),
            contentDescription = "$name profile",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier.width(115.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = handle,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
        }
        Column(horizontalAlignment = Alignment.Start) {
            Row {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.distance),
                    contentDescription = "Distance Icon",
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
            Text(
                text = level,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}


