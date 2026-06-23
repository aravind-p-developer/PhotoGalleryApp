package com.example.photogalleryapp.presentation.photodetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photogalleryapp.domain.model.Photo
import com.example.photogalleryapp.domain.usecase.GetFavoriteStatusUseCase
import com.example.photogalleryapp.domain.usecase.GetPhotoByIdUseCase
import com.example.photogalleryapp.domain.usecase.ToggleFavoriteUseCase
import com.example.photogalleryapp.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Photo Detail screen.
 * Loads a single photo instantly from the repository cache.
 */
@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val getPhotoByIdUseCase: GetPhotoByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Photo>>(UiState.Loading)
    val uiState: StateFlow<UiState<Photo>> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _toastMessage = kotlinx.coroutines.flow.MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun loadPhoto(photoId: Int) {
        val currentState = _uiState.value
        if (currentState is UiState.Success && currentState.data.id == photoId) {
            return
        }
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                // Instantly grab from cache (O(1)) instead of refetching everything
                val photo = getPhotoByIdUseCase(photoId)
                if (photo != null) {
                    _uiState.value = UiState.Success(photo)
                    // Load persisted favorite status from Room
                    _isFavorite.value = getFavoriteStatusUseCase(photoId)
                } else {
                    _uiState.value = UiState.Error("Photo not found")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load photo")
            }
        }
    }

    fun toggleFavorite() {
        val currentState = _uiState.value
        if (currentState !is UiState.Success) return

        viewModelScope.launch {
            try {
                toggleFavoriteUseCase(currentState.data)
                // Update local state to reflect the change immediately
                val newFavorite = !_isFavorite.value
                _isFavorite.value = newFavorite
                if (newFavorite) {
                    _toastMessage.emit("Added to favorites")
                } else {
                    _toastMessage.emit("Removed from favorites")
                }
            } catch (e: Exception) {
                _toastMessage.emit("Failed to update favorite status")
            }
        }
    }
}
