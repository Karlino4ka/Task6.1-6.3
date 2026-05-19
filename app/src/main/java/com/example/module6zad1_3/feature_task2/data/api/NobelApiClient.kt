package com.example.module6zad1_3.feature_task2.data.api

import com.example.module6zad1_3.feature_task2.data.dto.NobelPrizesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object NobelApiClient {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }

    suspend fun getNobelPrizes(): NobelPrizesResponse {
        return try {
            client.get("https://api.nobelprize.org/v1/prize.json").body()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getNobelPrizesByYear(year: Int): NobelPrizesResponse {
        return try {
            client.get("https://api.nobelprize.org/v1/prize.json?year=$year").body()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getNobelPrizesByCategory(category: String): NobelPrizesResponse {
        return try {
            client.get("https://api.nobelprize.org/v1/prize.json?category=$category").body()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}

