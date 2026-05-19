package com.example.module6zad1_3.feature_task2.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NobelPrizesResponse(
    @SerialName("prizes")
    val nobelPrizes: List<NobelPrizeDto> = emptyList()
)

@Serializable
data class NobelPrizeDto(
    @SerialName("year")
    val awardYear: String = "0",
    @SerialName("category")
    val category: String = "Unknown",
    @SerialName("laureates")
    val laureates: List<LaureateDto> = emptyList()
) {
    val categoryDto: CategoryDto
        get() = CategoryDto(category)
}

@Serializable
data class CategoryDto(
    @SerialName("english")
    val english: String = "Unknown"
)

@Serializable
data class LaureateDto(
    @SerialName("id")
    val id: String = "",
    @SerialName("firstname")
    val firstname: String = "",
    @SerialName("surname")
    val surname: String = "",
    @SerialName("motivation")
    val motivation: String = ""
) {
    val fullName: FullNameDto
        get() = FullNameDto(firstname + " " + surname)
}

@Serializable
data class FullNameDto(
    @SerialName("en")
    val en: String = "Unknown"
)

@Serializable
data class MotivationDto(
    @SerialName("en")
    val en: String = ""
)

@Serializable
data class BirthDto(
    @SerialName("country")
    val country: CountryDto? = null
)

@Serializable
data class CountryDto(
    @SerialName("en")
    val en: String = "Unknown"
)

@Serializable
data class LinksDto(
    @SerialName("wikipedia")
    val wikipedia: List<WikipediaDto>? = null
)

@Serializable
data class WikipediaDto(
    @SerialName("en")
    val en: String? = null
)

