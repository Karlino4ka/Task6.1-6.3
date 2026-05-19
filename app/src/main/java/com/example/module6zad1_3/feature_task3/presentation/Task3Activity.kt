package com.example.module6zad1_3.feature_task3.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.module6zad1_3.feature_task3.domain.model.User
import com.example.module6zad1_3.ui.theme.Module6Zad13Theme

class Task3Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Module6Zad13Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Task3Screen(
                        modifier = Modifier.padding(innerPadding),
                        onBackClick = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun Task3Screen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: Task3ViewModel = viewModel(factory = Task3ViewModelFactory(LocalContext.current))
) {
    val authState by viewModel.authState.collectAsState()
    var currentScreen by remember { mutableStateOf<String>("login") }

    androidx.compose.runtime.LaunchedEffect(authState) {
        when {
            authState == AuthUiState.Success && currentScreen == "login" -> {
                currentScreen = "users"
                viewModel.loadUsers()
            }
            authState == AuthUiState.Idle && currentScreen != "login" -> {
                currentScreen = "login"
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (currentScreen) {
            "login" -> LoginScreen(
                viewModel = viewModel,
                onBackClick = onBackClick
            )
            "users" -> UsersListScreen(
                viewModel = viewModel,
                onUserClick = { user ->
                    currentScreen = "user_detail_${user.id}"
                    viewModel.loadUserDetail(user.id)
                },
                onLogoutClick = {
                    viewModel.logout()
                    currentScreen = "login"
                }
            )
            else -> {
                if (currentScreen.startsWith("user_detail_")) {
                    UserDetailScreen(
                        viewModel = viewModel,
                        onBackClick = { currentScreen = "users" },
                        onLogoutClick = {
                            viewModel.logout()
                            currentScreen = "login"
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    viewModel: Task3ViewModel,
    onBackClick: () -> Unit
) {
    var username by remember { mutableStateOf("emilys") }
    var password by remember { mutableStateOf("emilyspass") }
    val authState by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        }

        Text("Login", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (authState) {
            is AuthUiState.Loading -> CircularProgressIndicator()
            is AuthUiState.Error -> {
                Text(
                    "Error: ${(authState as AuthUiState.Error).message}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
                Button(onClick = { viewModel.login(username, password) }) {
                    Text("Retry")
                }
            }
            else -> {
                Button(
                    onClick = { viewModel.login(username, password) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }
            }
        }
    }
}

@Composable
fun UsersListScreen(
    viewModel: Task3ViewModel,
    onUserClick: (User) -> Unit,
    onLogoutClick: () -> Unit
) {
    val usersState by viewModel.usersState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Users List", fontSize = 20.sp)
            Button(onClick = onLogoutClick) {
                Text("Logout")
            }
        }

        when (usersState) {
            is UsersUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UsersUiState.Success -> {
                val users = (usersState as UsersUiState.Success).users
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(users) { user ->
                        UserListItem(user = user, onClick = { onUserClick(user) })
                    }
                }
            }
            is UsersUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error: ${(usersState as UsersUiState.Error).message}")
                    Button(onClick = { viewModel.loadUsers() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!user.image.isNullOrEmpty()) {
            AsyncImage(
                model = user.image,
                contentDescription = user.firstName,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(user.firstName.first().uppercase())
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("${user.firstName} ${user.lastName}", fontSize = 14.sp)
            Text(user.username, fontSize = 12.sp)
            Text(user.email, fontSize = 11.sp)
        }
    }
}

@Composable
fun UserDetailScreen(
    viewModel: Task3ViewModel,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val userDetailState by viewModel.userDetailState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Button(onClick = onLogoutClick) {
                Text("Logout")
            }
        }

        when (userDetailState) {
            is UserDetailUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UserDetailUiState.Success -> {
                val user = (userDetailState as UserDetailUiState.Success).user
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!user.image.isNullOrEmpty()) {
                        AsyncImage(
                            model = user.image,
                            contentDescription = user.firstName,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("${user.firstName} ${user.lastName}", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("@${user.username}", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(user.email, fontSize = 12.sp)
                }
            }
            is UserDetailUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error: ${(userDetailState as UserDetailUiState.Error).message}")
                    Button(onClick = onBackClick) {
                        Text("Back")
                    }
                }
            }
        }
    }
}


class Task3ViewModelFactory(private val context: android.content.Context) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Task3ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return Task3ViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
