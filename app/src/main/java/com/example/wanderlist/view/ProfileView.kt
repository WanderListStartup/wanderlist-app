package com.example.wanderlist.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.wanderlist.R

@Preview(showBackground = true)
@Composable
fun ProfileViewPreview() {
    ProfileView()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView() {
    // Track which tab is selected
    val selectedTab = remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            // Optional top bar with WList logo
            CenterAlignedTopAppBar(
                title = { /* Could show "Profile" or remain empty */ },
                navigationIcon = {
                    Image(
                        painter = rememberAsyncImagePainter(R.drawable.wlist_logo),
                        contentDescription = "WList Logo",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .width(50.dp)
                            .height(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // 1) Profile Photo + Name/Handle + Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Picture
                Image(
                    painter = rememberAsyncImagePainter(
                        "https://upload.wikimedia.org/wikipedia/commons/4/45/A_small_cup_of_coffee.JPG"
                    ),
                    contentDescription = "User Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Name, handle, stats
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Jake Paul",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        fontSize = 24.sp
                    )

                    Text(
                        text = "@jtrex_44",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Followers / Following
                    Row(
                        modifier = Modifier
                            .padding(start = 40.dp, end = 40.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "15", fontWeight = FontWeight.Bold)
                            Text(text = "Followers", color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "20", fontWeight = FontWeight.Bold)
                            Text(text = "Following", color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Location
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Troy, NY",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                    }
                }
            }

            // 2) Level Row (Descriptor + Progress Bar)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Level 10",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.LightGray)
                ) {
                    // Filled portion
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.4f) // 40% progress for example
                            .background(Color(0xFF176FF2))
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "XP",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }

            // 3) Bio
            Text(
                text = "I am a huge foodie, local to the Troy Area! My goal is to collect as many badges as I can!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // 4) Edit Profile + Settings
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* Edit Profile logic */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Text(text = "Edit Profile", color = Color.Black)
                }

                Spacer(modifier = Modifier.width(16.dp))

                OutlinedButton(
                    onClick = { /* Some settings action */ },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(width = 50.dp, height = 50.dp)
                ) {
                    // Gear icon or text
                    Text("⚙") // Placeholder
                }
            }

            // 6) Tab Bar below "Add Friend"
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { selectedTab.value = 0 }
                ) {
                    Icon(Icons.Filled.Home, contentDescription = "Tab 0")
                }
                IconButton(
                    onClick = { selectedTab.value = 1 }
                ) {
                    Icon(Icons.Filled.Search, contentDescription = "Tab 1")
                }
                IconButton(
                    onClick = { selectedTab.value = 2 }
                ) {
                    Icon(Icons.Filled.Person, contentDescription = "Tab 2")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 7) Tab Content
            when (selectedTab.value) {
                0 -> {

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { /* Add Friend logic */ },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF176FF2)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 40.dp)
                    ) {
                        Text(text = "Add Friend")
                    }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        UserRow(
                            imageUrl = "https://example.com/logan_paul.jpg",
                            name = "Logan Paul",
                            handle = "@lPaul_45",
                            location = "Los Angeles, CA",
                            level = "Lvl: 2"
                        )
                        UserRow(
                            imageUrl = "https://example.com/walter_white.jpg",
                            name = "Walter White",
                            handle = "@heisenberg",
                            location = "Albuquerque, NM",
                            level = "Lvl: 999"
                        )
                        UserRow(
                            imageUrl = "https://example.com/angel_reese.jpg",
                            name = "Angel Reese",
                            handle = "@AngBricks",
                            location = "Chicago, IL",
                            level = "Lvl: 22"
                        )
                        UserRow(
                            imageUrl = "https://example.com/lebron_james.jpg",
                            name = "LeBron James",
                            handle = "@LeWanderer",
                            location = "Los Angeles, CA",
                            level = "Lvl: 999"
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
                1 -> {
                    // Show placeholder content for tab 1
                    Text(
                        text = "Search tab content here",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        textAlign = TextAlign.Center
                    )
                }
                2 -> {
                    // Show placeholder content for tab 2
                    Text(
                        text = "Profile tab content here",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// The same user row as before
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
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "$name profile",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = handle,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = location,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
            Text(
                text = level,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
