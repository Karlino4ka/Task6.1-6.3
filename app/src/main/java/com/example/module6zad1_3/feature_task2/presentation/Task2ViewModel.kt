package com.example.module6zad1_3.feature_task2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.module6zad1_3.feature_task2.domain.model.Laureate
import com.example.module6zad1_3.feature_task2.domain.usecase.GetLaureatesUseCase
import com.example.module6zad1_3.feature_task2.domain.usecase.GetLaureatesByCategoryUseCase
import com.example.module6zad1_3.feature_task2.domain.usecase.GetLaureatesByYearUseCase
import com.example.module6zad1_3.feature_task2.data.repository.LaureateRepositoryImpl

sealed class LaureateUiState {
    object Loading : LaureateUiState()
    data class Success(val laureates: List<Laureate>) : LaureateUiState()
    data class Error(val message: String) : LaureateUiState()
}

class Task2ViewModel : ViewModel() {
    private val repository = LaureateRepositoryImpl()
    private val getLaureatesUseCase = GetLaureatesUseCase(repository)
    private val getLaureatesByYearUseCase = GetLaureatesByYearUseCase(repository)
    private val getLaureatesByCategoryUseCase = GetLaureatesByCategoryUseCase(repository)

    private val _uiState = MutableStateFlow<LaureateUiState>(LaureateUiState.Loading)
    val uiState: StateFlow<LaureateUiState> = _uiState

    // Сохраняем все данные для комбинированной фильтрации
    private var allLaureates: List<Laureate> = emptyList()
    private var selectedCategory: String? = null
    private var selectedYear: Int? = null

    init {
        loadLaureates()
    }

    fun loadLaureates() {
        viewModelScope.launch {
            _uiState.value = LaureateUiState.Loading
            try {
                val laureates = getLaureatesUseCase()
                allLaureates = laureates
                selectedCategory = null
                selectedYear = null
                _uiState.value = LaureateUiState.Success(laureates)
            } catch (e: Exception) {
                _uiState.value = LaureateUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun filterByCategory(category: String?) {
        selectedCategory = category
        applyFilters()
    }

    fun filterByYear(year: Int?) {
        selectedYear = year
        applyFilters()
    }

    private fun applyFilters() {
        var filtered = allLaureates

        // Фильтруем по категории И году одновременно
        if (selectedCategory != null) {
            filtered = filtered.filter { it.category == selectedCategory }
        }

        if (selectedYear != null) {
            filtered = filtered.filter { it.awardYear == selectedYear }
        }

        _uiState.value = LaureateUiState.Success(filtered)
    }


    fun getAvailableYears(laureates: List<Laureate>): List<Int> {
        return laureates.map { it.awardYear }.distinct().sorted()
    }
}

