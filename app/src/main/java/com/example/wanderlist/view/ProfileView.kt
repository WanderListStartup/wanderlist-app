package com.example.wanderlist.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.wanderlist.R
import com.example.wanderlist.components.LikedPlacesGrid
import com.example.wanderlist.data.firestore.model.Badges
import com.example.wanderlist.ui.theme.wanderlistBlue
import com.example.wanderlist.viewmodel.EditProfileViewModel
import com.example.wanderlist.viewmodel.ProfileViewModel
import kotlin.math.floor


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
                .padding(horizontal = 16.dp)
        ) {
            // User details
            UserDetails(
                editProfileViewModel = editProfileViewModel,
                profileViewModel = profileViewModel,
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
                    .padding(bottom = 72.dp)
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
                        Text(
                            text = "Quest tab content here",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            textAlign = TextAlign.Center
                        )
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
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top // Align columns at the top for consistency
    ) {
        // Left Column: User image only
        Column(
            modifier = Modifier.width(140.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = editProfileViewModel.location,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.width(4.dp))

                Image(
                    painter = rememberAsyncImagePainter(R.drawable.distance),
                    contentDescription = "Distance Icon",
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Right Column: All user details
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {


            // Name and username
            Text(
                text = editProfileViewModel.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "@" + editProfileViewModel.username,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Friends count section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = profileViewModel.friendProfiles.size.toString(),
                        fontWeight = FontWeight.Bold)
                    Text(
                        color = Color.Gray,
                        text = "Friends",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = profileViewModel.likedEstablishments.size.toString(),
                        fontWeight = FontWeight.Bold)
                    Text(
                        color = Color.Gray,
                        text = "Liked Places",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Badge slots row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { index ->
                    val badge = profileViewModel.selectedBadges.getOrNull(index)
                    BadgeSlot(
                        badge = badge,
                        onClick = { profileViewModel.openBadgeDialog(index) }
                    )
                }
            }
            if (profileViewModel.slotIndexToSelect != null) {
                BadgeSelectionDialog(
                    slotIndex = profileViewModel.slotIndexToSelect!!,
                    allOwnedBadges = profileViewModel.userBadges,
                    selectedBadges = profileViewModel.selectedBadges,
                    onBadgeSelected = { chosenBadge ->
                        profileViewModel.onBadgeSelected(chosenBadge)
                    },
                    onDismiss = {
                        profileViewModel.closeBadgeDialog()
                    }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    // Row with level progress
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    )
    {
        LevelProgressView(profileViewModel = profileViewModel)
    }

    Spacer(modifier = Modifier.height(12.dp))
    // Bio section
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = editProfileViewModel.bio,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun BadgeSlot(
    badge: Badges?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .border(2.dp, Color.Gray, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (badge == null || badge.badgeId.isEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.plus_sign),
                contentDescription = "Empty Badge Slot",
                modifier = Modifier.size(24.dp)
            )
        } else {
            AsyncImage(
                model = badge.badgeImageUrl,
                contentDescription = badge.name,
                modifier = Modifier.size(38.dp)
            )
        }
    }
}


@Composable
fun BadgeSelectionDialog(
    slotIndex: Int,
    allOwnedBadges: List<Badges>,
    selectedBadges: List<Badges>,
    onBadgeSelected: (Badges) -> Unit,
    onDismiss: () -> Unit
) {
    // Get the badge currently assigned to the active slot (if any)
    val currentBadgeForSlot = selectedBadges.getOrNull(slotIndex)

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .heightIn(max = 500.dp)
            ) {
                Text(
                    text = "Select a Badge for Slot ${slotIndex + 1}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Build exactly 12 grid items.
                    items(12) { index ->
                        if (index < allOwnedBadges.size) {
                            val badge = allOwnedBadges[index]
                            // Determine if the badge is selected in any slot.
                            val isBadgeSelected = selectedBadges.any { it.badgeId == badge.badgeId }
                            // Determine if the badge is in the current slot being edited.
                            val isCurrentSlotSelected = currentBadgeForSlot?.badgeId == badge.badgeId

                            BadgeGridItem(
                                badge = badge,
                                isSelected = isBadgeSelected,
                                isCurrentSlotSelected = isCurrentSlotSelected,
                                onClick = { onBadgeSelected(badge) }
                            )
                        } else {
                            // Render a locked badge if the index exceeds the user's owned badge count.
                            val lockedBadge = Badges(
                                badgeId = "null", // Use a reserved/invalid ID for locked badges.
                                name = "Locked",
                                badgeImageUrl = "https://firebasestorage.googleapis.com/v0/b/wanderlist-b088c.firebasestorage.app/o/locked_badge.png?alt=media&token=ca84dd5a-dfcd-4433-886b-ef420aba51b3"
                            )
                            BadgeGridItem(
                                badge = lockedBadge,
                                isSelected = false,
                                isCurrentSlotSelected = false,
                                onClick = { /* Optionally indicate this badge is locked. */ }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BadgeGridItem(
    badge: Badges,
    isSelected: Boolean,
    isCurrentSlotSelected: Boolean,
    onClick: () -> Unit
) {
    // The container is clickable only if the badge isn't already used in another slot,
    // unless it is currently selected.
    Column(
        modifier = Modifier
            .padding(4.dp)
            .size(100.dp)
            .clickable(enabled = (!isSelected || isCurrentSlotSelected)) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
        ) {
            // The badge image fills the container.
            AsyncImage(
                model = badge.badgeImageUrl,
                contentDescription = badge.name,
                modifier = Modifier.fillMaxSize()
            )

            // Overlay an icon in the top right corner if this badge is selected.
            when {
                isCurrentSlotSelected -> {
                    // Current slot badge: show a circular green background with a checkmark.
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(24.dp)
                            .background(color = Color(0xFF338333), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected in current slot",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                isSelected -> {
                    // Badge already used in another slot: show a circular blue background with an 'x' icon.
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(24.dp)
                            .background(color = Color.DarkGray, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Selected in another slot",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = badge.name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
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
fun LevelProgressView(profileViewModel: ProfileViewModel) {
    val userLevel = profileViewModel.level

    // Display the "floored" level
    Text(
        text = "Level " + userLevel.toInt().toString(),
        fontSize = 20.sp,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
    )

    Spacer(modifier = Modifier.width(12.dp))

    // Calculate the fraction from 0.0 to 1.0
    val fraction = userLevel - floor(userLevel) // e.g., 10.6 -> 0.6

    // Display a progress bar that fills according to the fraction
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
                .fillMaxWidth(fraction.toFloat())  // 0.6 -> 60% filled
                .background(Color(0xFF176FF2))
        )
    }
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


