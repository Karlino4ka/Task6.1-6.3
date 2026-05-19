package com.example.module6zad1_3.feature_task1.domain.usecase

import com.example.module6zad1_3.feature_task1.data.repository.PhotoRepository
import com.example.module6zad1_3.feature_task1.domain.model.Photo

class GetPhotosUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(): List<Photo> {
        return repository.getPhotos()
    }
}

