package com.example.module6zad1_3.feature_task3.data.api

import com.example.module6zad1_3.feature_task3.data.dto.LoginRequest
import com.example.module6zad1_3.feature_task3.data.dto.LoginResponse
import com.example.module6zad1_3.feature_task3.data.dto.UserDto
import com.example.module6zad1_3.feature_task3.data.dto.UsersResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// ...existing code...

class UserApiClient(private val tokenProvider: suspend () -> String?) {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
        defaultRequest {
            url("https://dummyjson.com/")
        }
    }

    suspend fun login(username: String, password: String): LoginResponse {
        try {
            return client.post("auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }.body()
        } catch (e: Exception) {
            android.util.Log.e("UserApiClient", "Login error: ${e.message}")
            throw e
        }
    }

    suspend fun getUsers(): UsersResponse {
        val token = tokenProvider()
        return client.get("users") {
            if (token != null) {
                header("Authorization", "Bearer $token")
            }
        }.body()
    }

    suspend fun getUserById(id: Int): UserDto {
        val token = tokenProvider()
        return client.get("users/$id") {
            if (token != null) {
                header("Authorization", "Bearer $token")
            }
        }.body()
    }
}

