package com.example.wanderlist.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.wanderlist.R
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.viewmodel.FindFriendsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindFriendsView(
    onBackClick: () -> Unit = {},
    viewModel: FindFriendsViewModel = hiltViewModel()
) {
    val searchQuery = viewModel.searchQuery

    // 1) The users that are in MY incomingRequests
    val incomingRequests = viewModel.incomingRequestProfiles()

    // 2) Everyone else
    val otherProfiles = viewModel.otherProfilesExcludingRequests()

    Scaffold(
        containerColor = Color.White,
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1) The WList logo on top
            Image(
                painter = rememberAsyncImagePainter(R.drawable.bigwlist),
                contentDescription = "WList Logo",
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp)
                    .width(56.dp)
                    .height(43.dp),
                contentScale = ContentScale.Fit
            )

            // 2) A custom row that acts like your top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back arrow
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                // "Find Friends" text
                Text(
                    text = "Find Friends",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // 3) The rest of the UI (search bar + list)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    label = { Text("Search") },
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                // The actual list
                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    // A) Show incoming requests first
                    if (incomingRequests.isNotEmpty()) {
                        item {
                            Text(
                                text = "Incoming Requests",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(incomingRequests) { profile ->
                            FriendListItem(
                                userProfile = profile,
                                isIncomingRequest = true, // We pass a flag
                                onAddFriendClick = {
                                    // This means "Accept" in our logic
                                    viewModel.acceptFriendRequest(profile.uid)
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // B) Show everyone else below
                    if (otherProfiles.isNotEmpty()) {
                        item {
                            Text(
                                text = "Find Friends",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(otherProfiles) { profile ->
                            FriendListItem(
                                userProfile = profile,
                                isIncomingRequest = false,
                                onAddFriendClick = {
                                    // This means "Send request"
                                    viewModel.sendFriendRequest(profile)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FriendListItem(
    userProfile: UserProfile,
    isIncomingRequest: Boolean = false,  // <-- new parameter
    onAddFriendClick: () -> Unit
) {
    // Top spacer + divider
    Spacer(modifier = Modifier.height(10.dp))
    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

    // Outer Column for each list item
    Column {
        // Single Row that holds (Image) + (Two columns of text) + (AddFriend icon)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data("https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG")
                    .crossfade(true)
                    .build()
            )
            Image(
                painter = painter,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Middle "table": 2 equally-weighted columns side by side
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // LEFT COLUMN: Name / @username
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Name (with a minimal “(Incoming)” label if needed)
                    val nameText = if (isIncomingRequest) {
                        "${userProfile.name} (Incoming)"
                    } else {
                        userProfile.name
                    }

                    Text(
                        text = nameText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // @username
                    Text(
                        text = "@${userProfile.username}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // RIGHT COLUMN: Location / Lvl
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Location
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberAsyncImagePainter(R.drawable.distance),
                            contentDescription = "Distance Icon",
                            modifier = Modifier
                                .width(14.dp)
                                .height(14.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = userProfile.location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Level
                    Text(
                        text = "Lvl: ${userProfile.level}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(PaddingValues(start = 4.dp)),
                        maxLines = 1
                    )
                }
            }

            // Add Friend icon on the far right
            IconButton(onClick = onAddFriendClick) {
                Icon(
                    imageVector = Icons.Default.PersonAdd, // same icon
                    contentDescription = "Add Friend",
                    tint = Color.Gray
                )
            }
        }
    }
}
