package com.example.module6zad1_3.feature_task1.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    @SerialName("id")
    val id: String = "",
    @SerialName("author")
    val author: String = "Unknown",
    @SerialName("width")
    val width: Int = 600,
    @SerialName("height")
    val height: Int = 600,
    @SerialName("url")
    val url: String = "",
    @SerialName("download_url")
    val downloadUrl: String = "",
    // Дополнительные поля, которые могут быть в ответе
    @SerialName("post_url")
    val postUrl: String? = null
)

