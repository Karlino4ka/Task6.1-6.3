package com.example.module6zad1_3.feature_task2.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.module6zad1_3.feature_task2.domain.model.Laureate
import com.example.module6zad1_3.ui.theme.Module6Zad13Theme

class Task2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Module6Zad13Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Task2Screen(
                        modifier = Modifier.padding(innerPadding),
                        onBackClick = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun Task2Screen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: Task2ViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedLaureate by remember { mutableStateOf<Laureate?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedYear by remember { mutableStateOf<Int?>(null) }
    var showCategoryMenu by remember { mutableStateOf(false) }
    var showYearMenu by remember { mutableStateOf(false) }

    val categories = listOf("physics", "chemistry", "medicine", "literature", "peace", "economics")

    if (selectedLaureate != null) {
        LaureateDetailScreen(
            laureate = selectedLaureate!!,
            onBackClick = { selectedLaureate = null }
        )
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = { showCategoryMenu = !showCategoryMenu },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(selectedCategory ?: "Category")
                    }
                    DropdownMenu(
                        expanded = showCategoryMenu,
                        onDismissRequest = { showCategoryMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All Categories") },
                            onClick = {
                                selectedCategory = null
                                showCategoryMenu = false
                                viewModel.filterByCategory(null)
                            }
                        )
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    showCategoryMenu = false
                                    viewModel.filterByCategory(category)
                                }
                            )
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = { showYearMenu = !showYearMenu },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(selectedYear?.toString() ?: "Year")
                    }
                    DropdownMenu(
                        expanded = showYearMenu,
                        onDismissRequest = { showYearMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All Years") },
                            onClick = {
                                selectedYear = null
                                showYearMenu = false
                                viewModel.filterByYear(null)
                            }
                        )
                        val availableYears = if (uiState is LaureateUiState.Success) {
                            viewModel.getAvailableYears((uiState as LaureateUiState.Success).laureates).reversed()
                        } else {
                            emptyList()
                        }
                        availableYears.forEach { year ->
                            DropdownMenuItem(
                                text = { Text(year.toString()) },
                                onClick = {
                                    selectedYear = year
                                    showYearMenu = false
                                    viewModel.filterByYear(year)
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = { viewModel.loadLaureates() },
                    modifier = Modifier.weight(0.8f)
                ) {
                    Text("Refresh")
                }
            }

            when (uiState) {
                is LaureateUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LaureateUiState.Success -> {
                    val laureates = (uiState as LaureateUiState.Success).laureates
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(laureates) { laureate ->
                            LaureateListItem(
                                laureate = laureate,
                                onClick = { selectedLaureate = laureate }
                            )
                        }
                    }
                }

                is LaureateUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Error: ${(uiState as LaureateUiState.Error).message}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = { viewModel.loadLaureates() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LaureateListItem(
    laureate: Laureate,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Text(text = laureate.fullName, fontSize = 16.sp)
        Text(text = "Year: ${laureate.awardYear} | Category: ${laureate.category}", fontSize = 12.sp)
        Text(
            text = laureate.motivation,
            fontSize = 11.sp,
            maxLines = 2
        )
    }
}

@Composable
fun LaureateDetailScreen(
    laureate: Laureate,
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

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(laureate.fullName, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Year: ${laureate.awardYear}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Category: ${laureate.category}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Country: ${laureate.birthCountry}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Motivation:", fontSize = 14.sp)
            Text(laureate.motivation, fontSize = 12.sp)
        }
    }
}

