package com.example.module6zad1_3.feature_task1.data.api

import com.example.module6zad1_3.feature_task1.data.dto.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumApi {
    @GET("v2/list")
    suspend fun getPhotos(
        @Query("limit") limit: Int = 30,
        @Query("page") page: Int = 1
    ): List<PhotoDto>
}

