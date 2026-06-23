package com.example.photogalleryapp.presentation.createpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photogalleryapp.domain.model.Post
import com.example.photogalleryapp.domain.usecase.CreatePostUseCase
import com.example.photogalleryapp.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Create Post screen.
 * Manages form state, validation, submission, and session-only post history.
 */
@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase
) : ViewModel() {

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    // Form field states
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _body = MutableStateFlow("")
    val body: StateFlow<String> = _body.asStateFlow()

    private val _titleError = MutableStateFlow<String?>(null)
    val titleError: StateFlow<String?> = _titleError.asStateFlow()

    private val _bodyError = MutableStateFlow<String?>(null)
    val bodyError: StateFlow<String?> = _bodyError.asStateFlow()

    // Submission state
    private val _submitState = MutableStateFlow<UiState<Post>?>(null)
    val submitState: StateFlow<UiState<Post>?> = _submitState.asStateFlow()

    // In-memory list of posts created during this session
    private val _sessionPosts = MutableStateFlow<List<Post>>(emptyList())
    val sessionPosts: StateFlow<List<Post>> = _sessionPosts.asStateFlow()

    fun onTitleChanged(value: String) {
        _title.value = value
        if (value.isNotBlank()) _titleError.value = null
    }

    fun onBodyChanged(value: String) {
        _body.value = value
        if (value.isNotBlank()) _bodyError.value = null
    }

    fun submitPost() {
        // Validate before submitting
        var isValid = true
        if (_title.value.isBlank()) {
            _titleError.value = "Title cannot be empty"
            isValid = false
        }
        if (_body.value.isBlank()) {
            _bodyError.value = "Body cannot be empty"
            isValid = false
        }
        if (!isValid) {
            viewModelScope.launch {
                _toastMessage.emit("Title and body are required")
            }
            return
        }

        viewModelScope.launch {
            _submitState.value = UiState.Loading
            try {
                val post = createPostUseCase(_title.value, _body.value)
                _submitState.value = UiState.Success(post)
                _toastMessage.emit("Post created successfully")
                // Add to the in-session list
                _sessionPosts.value = listOf(post) + _sessionPosts.value
                // Reset form fields after success
                _title.value = ""
                _body.value = ""
            } catch (e: Exception) {
                _submitState.value = UiState.Error(e.message ?: "Failed to create post")
                _toastMessage.emit("Failed to create post")
            }
        }
    }

    /** Resets the submit state so the success message can be dismissed. */
    fun clearSubmitState() {
        _submitState.value = null
    }
}
