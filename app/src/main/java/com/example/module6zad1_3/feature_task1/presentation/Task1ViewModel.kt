package com.example.module6zad1_3.feature_task1.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import com.example.module6zad1_3.feature_task1.domain.model.Photo
import com.example.module6zad1_3.feature_task1.domain.usecase.GetPhotosUseCase
import com.example.module6zad1_3.feature_task1.data.api.PicsumApi
import com.example.module6zad1_3.feature_task1.data.repository.PhotoRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

sealed class PhotoUiState {
    object Loading : PhotoUiState()
    data class Success(val photos: List<Photo>) : PhotoUiState()
    data class Error(val message: String) : PhotoUiState()
}

class Task1ViewModel(private val getPhotosUseCase: GetPhotosUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<PhotoUiState>(PhotoUiState.Loading)
    val uiState: StateFlow<PhotoUiState> = _uiState

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _uiState.value = PhotoUiState.Loading
            try {
                val photos = getPhotosUseCase()
                _uiState.value = PhotoUiState.Success(photos)
            } catch (e: Exception) {
                _uiState.value = PhotoUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

class Task1ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Task1ViewModel::class.java)) {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val newRequest = originalRequest.newBuilder()
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .header("Accept", "*/*")
                        .header("Accept-Language", "en-US,en;q=0.9")
                        .header("Connection", "keep-alive")
                        .header("Referer", "https://picsum.photos/")
                        .header("Sec-Fetch-Dest", "empty")
                        .header("Sec-Fetch-Mode", "cors")
                        .header("Sec-Fetch-Site", "same-origin")
                        .removeHeader("Accept-Encoding")
                        .build()

                    try {
                        val response = chain.proceed(newRequest)
                        val body = response.peekBody(Long.MAX_VALUE).string()
                        android.util.Log.d("Task1API", "Request: ${newRequest.url}, Status: ${response.code}")
                        if (body.isNotEmpty()) {
                            android.util.Log.d("Task1API", "Body: ${body.take(300)}")
                        }
                        response
                    } catch (e: Exception) {
                        android.util.Log.e("Task1API", "Request failed: ${newRequest.url}, Error: ${e.message}")
                        throw e
                    }
                }
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            val json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                prettyPrint = false
                isLenient = true
            }
            val retrofit = Retrofit.Builder()
                .baseUrl("https://picsum.photos/")
                .client(okHttpClient)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()

            val api = retrofit.create(PicsumApi::class.java)
            val repository = PhotoRepositoryImpl(api)
            val useCase = GetPhotosUseCase(repository)

            @Suppress("UNCHECKED_CAST")
            return Task1ViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

