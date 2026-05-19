package com.example.module6zad1_3.feature_task2.domain.usecase

import com.example.module6zad1_3.feature_task2.data.repository.LaureateRepository
import com.example.module6zad1_3.feature_task2.domain.model.Laureate

class GetLaureatesUseCase(private val repository: LaureateRepository) {
    suspend operator fun invoke(): List<Laureate> {
        return repository.getLaureates()
    }
}

class GetLaureatesByYearUseCase(private val repository: LaureateRepository) {
    suspend operator fun invoke(year: Int): List<Laureate> {
        return repository.getLaureatesByYear(year)
    }
}

class GetLaureatesByCategoryUseCase(private val repository: LaureateRepository) {
    suspend operator fun invoke(category: String): List<Laureate> {
        return repository.getLaureatesByCategory(category)
    }
}

