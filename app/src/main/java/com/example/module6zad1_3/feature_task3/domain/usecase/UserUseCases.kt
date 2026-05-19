package com.example.module6zad1_3.feature_task3.domain.usecase

import com.example.module6zad1_3.feature_task3.data.repository.AuthRepository
import com.example.module6zad1_3.feature_task3.data.repository.UserRepository
import com.example.module6zad1_3.feature_task3.domain.model.AuthToken
import com.example.module6zad1_3.feature_task3.domain.model.User

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): AuthToken {
        return authRepository.login(username, password)
    }
}

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}

class GetUsersUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): List<User> {
        return userRepository.getUsers()
    }
}

class GetUserByIdUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: Int): User {
        return userRepository.getUserById(id)
    }
}

