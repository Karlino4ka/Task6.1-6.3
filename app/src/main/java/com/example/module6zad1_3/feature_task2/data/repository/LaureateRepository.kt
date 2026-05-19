package com.example.module6zad1_3.feature_task2.data.repository

import com.example.module6zad1_3.feature_task2.data.api.NobelApiClient
import com.example.module6zad1_3.feature_task2.domain.model.Laureate

interface LaureateRepository {
    suspend fun getLaureates(): List<Laureate>
    suspend fun getLaureatesByYear(year: Int): List<Laureate>
    suspend fun getLaureatesByCategory(category: String): List<Laureate>
}

class LaureateRepositoryImpl : LaureateRepository {
    override suspend fun getLaureates(): List<Laureate> {
        val response = NobelApiClient.getNobelPrizes()
        return response.nobelPrizes.flatMap { prize ->
            prize.laureates.mapNotNull { laureate ->
                val fullName = (laureate.firstname + " " + laureate.surname).trim()
                if (fullName.isEmpty()) return@mapNotNull null

                Laureate(
                    id = laureate.id,
                    fullName = fullName,
                    awardYear = prize.awardYear.toIntOrNull() ?: 0,
                    category = prize.category,
                    motivation = laureate.motivation.take(100).ifEmpty { "No motivation" },
                    birthCountry = "Unknown"
                )
            }
        }
    }

    override suspend fun getLaureatesByYear(year: Int): List<Laureate> {
        val response = NobelApiClient.getNobelPrizesByYear(year)
        return response.nobelPrizes.flatMap { prize ->
            prize.laureates.mapNotNull { laureate ->
                val fullName = (laureate.firstname + " " + laureate.surname).trim()
                if (fullName.isEmpty()) return@mapNotNull null

                Laureate(
                    id = laureate.id,
                    fullName = fullName,
                    awardYear = prize.awardYear.toIntOrNull() ?: 0,
                    category = prize.category,
                    motivation = laureate.motivation.take(100).ifEmpty { "No motivation" },
                    birthCountry = "Unknown"
                )
            }
        }
    }

    override suspend fun getLaureatesByCategory(category: String): List<Laureate> {
        val response = NobelApiClient.getNobelPrizesByCategory(category)
        return response.nobelPrizes.flatMap { prize ->
            prize.laureates.mapNotNull { laureate ->
                val fullName = (laureate.firstname + " " + laureate.surname).trim()
                if (fullName.isEmpty()) return@mapNotNull null

                Laureate(
                    id = laureate.id,
                    fullName = fullName,
                    awardYear = prize.awardYear.toIntOrNull() ?: 0,
                    category = prize.category,
                    motivation = laureate.motivation.take(100).ifEmpty { "No motivation" },
                    birthCountry = "Unknown"
                )
            }
        }
    }
}

