package com.example.module6zad1_3.feature_task3.domain.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val image: String? = null
)

data class AuthToken(
    val token: String,
    val userId: Int
)

