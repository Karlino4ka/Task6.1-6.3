package com.example.module6zad1_3.feature_task3.data.repository

import com.example.module6zad1_3.feature_task3.data.api.UserApiClient
import com.example.module6zad1_3.feature_task3.data.datastore.TokenDataStore
import com.example.module6zad1_3.feature_task3.domain.model.AuthToken
import com.example.module6zad1_3.feature_task3.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthToken
    suspend fun logout()
    suspend fun getToken(): String?
}

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUserById(id: Int): User
}

class AuthRepositoryImpl(
    private val api: UserApiClient,
    private val tokenDataStore: TokenDataStore
) : AuthRepository {
    override suspend fun login(username: String, password: String): AuthToken {
        val response = api.login(username, password)
        val token = response.token
        tokenDataStore.saveToken(token, response.id)
        return AuthToken(token, response.id)
    }

    override suspend fun logout() {
        tokenDataStore.clearToken()
    }

    override suspend fun getToken(): String? {
        return tokenDataStore.getToken()
    }
}

class UserRepositoryImpl(
    private val api: UserApiClient,
    private val authRepository: AuthRepository
) : UserRepository {
    override suspend fun getUsers(): List<User> {
        val response = api.getUsers()
        return response.users.map { dto ->
            User(
                id = dto.id,
                username = dto.username,
                email = dto.email,
                firstName = dto.firstName,
                lastName = dto.lastName,
                image = dto.image
            )
        }
    }

    override suspend fun getUserById(id: Int): User {
        val dto = api.getUserById(id)
        return User(
            id = dto.id,
            username = dto.username,
            email = dto.email,
            firstName = dto.firstName,
            lastName = dto.lastName,
            image = dto.image
        )
    }
}

