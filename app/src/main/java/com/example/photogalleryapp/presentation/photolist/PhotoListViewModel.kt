package com.example.photogalleryapp.presentation.photolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photogalleryapp.domain.model.Photo
import com.example.photogalleryapp.domain.usecase.GetPhotosUseCase
import com.example.photogalleryapp.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Photo List screen.
 * Manages fetching, searching, and filtering of photos using StateFlow operators.
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _allPhotos = MutableStateFlow<List<Photo>>(emptyList())
    private val _isLoading = MutableStateFlow(true)
    private val _errorMessage = MutableStateFlow<String?>(null)

    // Combines photos and debounced query, and runs the filtering on Dispatchers.Default
    val uiState: StateFlow<UiState<List<Photo>>> = combine(
        _allPhotos,
        _searchQuery.debounce(300),
        _isLoading,
        _errorMessage
    ) { photos, query, isLoading, errorMessage ->
        when {
            isLoading -> UiState.Loading
            errorMessage != null -> UiState.Error(errorMessage)
            else -> UiState.Success(filterPhotos(photos, query))
        }
    }
    .flowOn(Dispatchers.Default)
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _allPhotos.value = getPhotosUseCase()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load photos. Please check your connection."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    /** Filters photos by title (case-insensitive). Runs on Dispatchers.Default via flowOn. */
    private fun filterPhotos(photos: List<Photo>, query: String): List<Photo> {
        if (query.isBlank()) return photos
        return photos.filter { it.title.contains(query.trim(), ignoreCase = true) }
    }
}
