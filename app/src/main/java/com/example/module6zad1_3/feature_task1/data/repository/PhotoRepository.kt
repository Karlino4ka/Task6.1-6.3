package com.example.module6zad1_3.feature_task1.data.repository

import com.example.module6zad1_3.feature_task1.data.api.PicsumApi
import com.example.module6zad1_3.feature_task1.domain.model.Photo

interface PhotoRepository {
    suspend fun getPhotos(): List<Photo>
}

class PhotoRepositoryImpl(private val api: PicsumApi) : PhotoRepository {
    override suspend fun getPhotos(): List<Photo> {
        return api.getPhotos(limit = 30, page = 1).map { dto ->
            Photo(
                id = dto.id,
                author = dto.author,
                width = dto.width,
                height = dto.height,
                url = dto.url,
                downloadUrl = dto.downloadUrl
            )
        }
    }
}

