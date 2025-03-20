package com.example.wanderlist.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wanderlist.viewmodel.PlacesViewModel

@Composable
fun PlacesScreen(viewModel: PlacesViewModel = viewModel()) {
    // Collect places state from the ViewModel.
    val places = viewModel.places.collectAsState().value

    // Display the list of places (using LazyColumn for example).
    LazyColumn {
        items(places) { place ->
            Text(text = place.displayName ?: "No Name")
        }
    }
}
