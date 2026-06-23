package com.example.photogalleryapp.presentation.common

/**
 * Sealed class representing the three possible UI states for any screen.
 * Using a generic type makes this reusable across all screens.
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
