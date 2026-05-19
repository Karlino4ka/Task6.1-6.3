package com.example.module6zad1_3.feature_task3.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.module6zad1_3.feature_task3.data.api.UserApiClient
import com.example.module6zad1_3.feature_task3.data.datastore.TokenDataStore
import com.example.module6zad1_3.feature_task3.data.repository.AuthRepositoryImpl
import com.example.module6zad1_3.feature_task3.data.repository.UserRepositoryImpl
import com.example.module6zad1_3.feature_task3.domain.model.User
import com.example.module6zad1_3.feature_task3.domain.usecase.GetUserByIdUseCase
import com.example.module6zad1_3.feature_task3.domain.usecase.GetUsersUseCase
import com.example.module6zad1_3.feature_task3.domain.usecase.LoginUseCase
import com.example.module6zad1_3.feature_task3.domain.usecase.LogoutUseCase

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

sealed class UsersUiState {
    object Loading : UsersUiState()
    data class Success(val users: List<User>) : UsersUiState()
    data class Error(val message: String) : UsersUiState()
}

sealed class UserDetailUiState {
    object Loading : UserDetailUiState()
    data class Success(val user: User) : UserDetailUiState()
    data class Error(val message: String) : UserDetailUiState()
}

class Task3ViewModel(context: Context) : ViewModel() {
    private val tokenDataStore = TokenDataStore(context)
    private val apiClient = UserApiClient { tokenDataStore.getToken() }
    private val authRepository = AuthRepositoryImpl(apiClient, tokenDataStore)
    private val userRepository = UserRepositoryImpl(apiClient, authRepository)

    private val loginUseCase = LoginUseCase(authRepository)
    private val logoutUseCase = LogoutUseCase(authRepository)
    private val getUsersUseCase = GetUsersUseCase(userRepository)
    private val getUserByIdUseCase = GetUserByIdUseCase(userRepository)

    private val _authState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authState: StateFlow<AuthUiState> = _authState

    private val _usersState = MutableStateFlow<UsersUiState>(UsersUiState.Loading)
    val usersState: StateFlow<UsersUiState> = _usersState

    private val _userDetailState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Loading)
    val userDetailState: StateFlow<UserDetailUiState> = _userDetailState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            try {
                loginUseCase(username, password)
                _authState.value = AuthUiState.Success
            } catch (e: Exception) {
                _authState.value = AuthUiState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                logoutUseCase()
                _authState.value = AuthUiState.Idle
            } catch (e: Exception) {
                _authState.value = AuthUiState.Error(e.message ?: "Logout failed")
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = UsersUiState.Loading
            try {
                val users = getUsersUseCase()
                _usersState.value = UsersUiState.Success(users)
            } catch (e: Exception) {
                _usersState.value = UsersUiState.Error(e.message ?: "Failed to load users")
            }
        }
    }

    fun loadUserDetail(id: Int) {
        viewModelScope.launch {
            _userDetailState.value = UserDetailUiState.Loading
            try {
                val user = getUserByIdUseCase(id)
                _userDetailState.value = UserDetailUiState.Success(user)
            } catch (e: Exception) {
                _userDetailState.value = UserDetailUiState.Error(e.message ?: "Failed to load user")
            }
        }
    }
}

