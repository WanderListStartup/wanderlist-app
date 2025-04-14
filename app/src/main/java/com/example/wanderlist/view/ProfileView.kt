package com.example.wanderlist.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.wanderlist.R
import com.example.wanderlist.components.LikedPlacesGrid
import com.example.wanderlist.components.ProfileReviewCard
import com.example.wanderlist.ui.theme.wanderlistBlue
import com.example.wanderlist.viewmodel.EditProfileViewModel
import com.example.wanderlist.viewmodel.ProfileViewModel


@Composable
fun ProfileView(
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToLikedPlace: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToUserSettings: () -> Unit,
    onNavigateToFindFriends: () -> Unit,
) {
    MaterialTheme {
        ProfileScreen(
            editProfileViewModel = editProfileViewModel,
            profileViewModel = profileViewModel,
            onNavigateToLikedPlace = {
                establishmentId -> onNavigateToLikedPlace(establishmentId)
            },
            onNavigateToHome = onNavigateToHome,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToUserSettings = onNavigateToUserSettings,
            onNavigateToFindFriends = onNavigateToFindFriends,
        )
    }
}

@Composable
fun ProfileScreen(
    editProfileViewModel: EditProfileViewModel,
    profileViewModel: ProfileViewModel,
    onNavigateToLikedPlace: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToUserSettings: () -> Unit,
    onNavigateToFindFriends: () -> Unit,
) {

    Scaffold(
        containerColor = Color.White,
        topBar = { WlTopBar() },
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
            // User details
            UserDetails(
                editProfileViewModel = editProfileViewModel,
            )


            // Edit profile button and settings button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(start = 45.dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onNavigateToUserSettings() },
                    modifier = Modifier
                        .height(33.dp)
                        .shadow(elevation=6.dp, shape= RoundedCornerShape(20.dp))
                        .width(240.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Edit Profile", color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterVertically),)
                }
                IconButton(
                    onClick = { onNavigateToSettings() },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Black)
                ) {
                   Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                }
            }

            Row(
                modifier = Modifier.width(350.dp).align(Alignment.CenterHorizontally)
            )
            {
                HorizontalDivider(modifier = Modifier.padding(top=15.dp, bottom=10.dp))
            }

            // Selecting between Tabs
            val selectedTab = profileViewModel.selectedTab
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = { profileViewModel.updateSelectedTab(0) }
                ) {
                    Icon(Icons.Rounded.Favorite, contentDescription = "Tab 0")
                }
                IconButton(
                    onClick = { profileViewModel.updateSelectedTab(1) }
                ) {
                    Icon(Icons.AutoMirrored.Rounded.Assignment, contentDescription = "Tab 1")
                }
                IconButton(
                    onClick = { profileViewModel.updateSelectedTab(2) }
                ) {
                    Icon(Icons.Rounded.Group, contentDescription = "Tab 2")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Tab content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 30.dp)
            ) {
                when (selectedTab) {
                    0 -> {
                        val likedEstablishmentsDetails = profileViewModel.likedEstablishmentsDetails
                        LikedPlacesGrid(
                            likedEstablishmentsDetails,
                            onNavigateToLikedPlace = {
                                    establishmentId -> onNavigateToLikedPlace(establishmentId)
                            },
                        )
                    }
                    1 -> {
                        val reviews = profileViewModel.reviewsForUser
                        Column {

                            reviews.forEach { review ->
                                val displayName = profileViewModel.establishmentNames[review.establishmentId] ?: "Unknown"
                                val location = "Troy, NY"
                                ProfileReviewCard(
                                    establishmentName = displayName,
                                    location = location,
                                    rating = review.rating,
                                    reviewText = review.reviewText,
                                    onEditClick = {  },
                                    onDeleteClick = { },
                                )

                                HorizontalDivider()
                            }
                        }
                    }
                    2 -> {
                        FriendsTab(
                            profileViewModel = profileViewModel,
                            onNavigateToFindFriends = onNavigateToFindFriends
                        )
                    }
                }
            }
        }
    }
}

// TOP AND BOTTOM BARS
@Composable
fun WlTopBar() {
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
fun UserDetails(
    editProfileViewModel: EditProfileViewModel,
) {
    // Profile picture and user details
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile picture
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

        // User details
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = editProfileViewModel.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                fontSize = 32.sp,
                modifier = Modifier.padding(PaddingValues(start = 36.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = editProfileViewModel.username,
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
    // Location and level
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
            text = editProfileViewModel.location,
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
        Spacer(modifier = Modifier.width(12.dp))
    }

    Text(
        text = editProfileViewModel.bio,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(PaddingValues(start = 15.dp, end = 30.dp))
    )
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun FriendsTab(
    profileViewModel: ProfileViewModel,
    onNavigateToFindFriends: () -> Unit,
){
    Spacer(modifier = Modifier.height(15.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // "Add Friend" button (unchanged)
        Button(
            onClick = { onNavigateToFindFriends() },
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
    val friendProfiles = profileViewModel.friendProfiles

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp)
    ) {
        friendProfiles.forEach { friend ->
            UserRow(
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG",
                name = friend.name,
                handle = "@${friend.username}",
                location = friend.location,
                level = "Lvl: ${friend.level}",
                onRemoveFriendClick = {
                    // This means "Accept" in our logic
                    profileViewModel.removeFriend(friend.uid)
                }
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.2f)
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun UserRow(
    imageUrl: String,
    name: String,
    handle: String,
    location: String,
    level: String,
    onRemoveFriendClick: () -> Unit
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

        Spacer(modifier = Modifier.width(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { onRemoveFriendClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.PersonRemove,
                    contentDescription = "Remove Friend",
                    tint = Color.Red
                )
            }
        }
    }
}


