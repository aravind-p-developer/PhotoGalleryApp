package com.example.photogalleryapp.presentation.uploadphoto

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photogalleryapp.domain.model.UploadResponse
import com.example.photogalleryapp.domain.usecase.UploadPhotoUseCase
import com.example.photogalleryapp.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

/**
 * ViewModel for the Photo Upload screen.
 * Handles gallery URI resolution, multipart body creation, and upload state.
 */
@HiltViewModel
class UploadPhotoViewModel @Inject constructor(
    private val uploadPhotoUseCase: UploadPhotoUseCase
) : ViewModel() {

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    private val _uploadState = MutableStateFlow<UiState<UploadResponse>?>(null)
    val uploadState: StateFlow<UiState<UploadResponse>?> = _uploadState.asStateFlow()

    fun onImageSelected(uri: Uri) {
        _selectedImageUri.value = uri
        // Reset any previous upload result when a new image is selected
        _uploadState.value = null
    }

    fun uploadPhoto(context: Context) {
        val uri = _selectedImageUri.value ?: return

        viewModelScope.launch {
            _uploadState.value = UiState.Loading
            try {
                val part = withContext(Dispatchers.IO) {
                    createMultipartBody(context, uri)
                }
                val response = uploadPhotoUseCase(part)
                _uploadState.value = UiState.Success(response)
                _toastMessage.emit("Upload successful")
            } catch (e: retrofit2.HttpException) {
                val errMsg = if (e.code() == 503) {
                    "Upload service unavailable (HTTP 503). Please try again later."
                } else {
                    "Image upload failed."
                }
                _uploadState.value = UiState.Error(errMsg)
                _toastMessage.emit(errMsg)
            } catch (e: java.io.IOException) {
                val errMsg = "Network connection unavailable."
                _uploadState.value = UiState.Error(errMsg)
                _toastMessage.emit(errMsg)
            } catch (e: Exception) {
                val errMsg = "Image upload failed."
                _uploadState.value = UiState.Error(errMsg)
                _toastMessage.emit(errMsg)
            }
        }
    }

    /** Reads the image bytes from the content URI and wraps them in a MultipartBody.Part. */
    private fun createMultipartBody(context: Context, uri: Uri): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
        val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: throw IllegalStateException("Cannot read image file")
        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", "upload.jpg", requestBody)
    }
}
