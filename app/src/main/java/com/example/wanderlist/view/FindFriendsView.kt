package com.example.wanderlist.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.wanderlist.data.firestore.model.UserProfile
import com.example.wanderlist.viewmodel.FindFriendsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindFriendsView(
    onBackClick: () -> Unit = {},  // You can pass a callback to handle back navigation
    viewModel: FindFriendsViewModel = hiltViewModel()
) {
    // Pinkish gradient background
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFE3E8),  // top color
            Color(0xFFFCC7D2)   // bottom color
        )
    )

    // Current search text and filtered list
    val searchQuery = viewModel.searchQuery
    val filteredProfiles = viewModel.filteredProfiles()

    // M3 Scaffold uses containerColor, not backgroundColor
    Scaffold(
        topBar = {
            FindFriendsTopBar(onBackClick)
        },
        containerColor = Color.Transparent // We'll apply gradient manually
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // The search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    label = { Text("Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true
                )

                // Display the matching user profiles
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredProfiles) { profile ->
                        FriendListItem(
                            userProfile = profile,
                            onAddFriendClick = {
                                // TODO: implement "Add Friend" logic here
                            }
                        )
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// A custom top bar matching your screenshot style (Material 3 version)
// --------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindFriendsTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        // M3 TopAppBar does not have backgroundColor/elevation params
        // Instead, we specify the colors via TopAppBarDefaults
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black
        ),
        title = {
            // M3 no longer provides h6 by default.
            // You can pick whichever text style suits you best:
            // e.g. titleLarge, titleMedium, bodyLarge, etc.
            Text(
                text = "Find Friends",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

// --------------------------------------------------------------------
// A single row in the "find friends" list
// --------------------------------------------------------------------
@Composable
fun FriendListItem(
    userProfile: UserProfile,
    onAddFriendClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile picture on the left
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data("https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG") // or your default URL
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

            // Name, @username, location, level in a column
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                // M3 typically doesn't have 'subtitle1'; pick something like bodyLarge
                Text(
                    text = userProfile.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                // There's no 'caption' in M3; use e.g. bodySmall or labelSmall
                Text(
                    text = "@${userProfile.username}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "${userProfile.location}, Lvl: 999",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Add Friend icon on the right
            IconButton(onClick = onAddFriendClick) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Add Friend",
                    tint = Color.Black
                )
            }
        }
        // Use M3 Divider
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
    }
}
