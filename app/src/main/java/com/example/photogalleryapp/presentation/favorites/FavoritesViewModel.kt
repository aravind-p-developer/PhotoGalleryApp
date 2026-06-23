package com.example.photogalleryapp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photogalleryapp.domain.model.Photo
import com.example.photogalleryapp.domain.usecase.GetFavoritePhotosUseCase
import com.example.photogalleryapp.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritePhotosUseCase: GetFavoritePhotosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Photo>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Photo>>> = _uiState

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val favorites = getFavoritePhotosUseCase()
                _uiState.value = UiState.Success(favorites)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load favorites")
            }
        }
    }
}
