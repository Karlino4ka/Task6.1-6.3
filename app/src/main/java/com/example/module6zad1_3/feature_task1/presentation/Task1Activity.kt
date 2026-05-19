package com.example.module6zad1_3.feature_task1.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.module6zad1_3.feature_task1.domain.model.Photo
import com.example.module6zad1_3.ui.theme.Module6Zad13Theme

class Task1Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Module6Zad13Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Task1Screen(
                        modifier = Modifier.padding(innerPadding),
                        onBackClick = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun Task1Screen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: Task1ViewModel = viewModel(factory = Task1ViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedPhoto by remember { mutableStateOf<Photo?>(null) }

    if (selectedPhoto != null) {
        PhotoDetailScreen(
            photo = selectedPhoto!!,
            onBackClick = { selectedPhoto = null }
        )
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }

            when (uiState) {
                is PhotoUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is PhotoUiState.Success -> {
                    val photos = (uiState as PhotoUiState.Success).photos
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(photos) { photo ->
                            PhotoGridItem(
                                photo = photo,
                                onClick = { selectedPhoto = photo }
                            )
                        }
                    }
                }

                is PhotoUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Error: ${(uiState as PhotoUiState.Error).message}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = { viewModel.loadPhotos() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoGridItem(
    photo: Photo,
    onClick: () -> Unit
) {
    // Используем downloadUrl так как он работает (проверено на экране деталей)
    val imageUrl = if (photo.downloadUrl.isNotEmpty()) {
        photo.downloadUrl
    } else if (photo.url.isNotEmpty()) {
        photo.url
    } else {
        ""
    }

    android.util.Log.d("PhotoGridItem", "Loading image: $imageUrl for ${photo.author}")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = photo.author,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                .padding(8.dp)
        ) {
            Text(
                text = photo.author,
                fontSize = 12.sp,
                maxLines = 1
            )
            Text(
                text = "${photo.width}×${photo.height}",
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun PhotoDetailScreen(
    photo: Photo,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
        }

        AsyncImage(
            model = photo.downloadUrl,
            contentDescription = photo.author,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Author: ${photo.author}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Dimensions: ${photo.width}×${photo.height}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("URL: ${photo.downloadUrl}", fontSize = 12.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Download logic will be added */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Download Photo")
            }
        }
    }
}

