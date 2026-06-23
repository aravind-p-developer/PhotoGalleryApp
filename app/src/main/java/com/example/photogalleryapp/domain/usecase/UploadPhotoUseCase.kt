package com.example.photogalleryapp.domain.usecase

import com.example.photogalleryapp.domain.model.UploadResponse
import com.example.photogalleryapp.domain.repository.UploadRepository
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Use case to upload a photo to the server.
 */
class UploadPhotoUseCase @Inject constructor(
    private val uploadRepository: UploadRepository
) {
    suspend operator fun invoke(part: MultipartBody.Part): UploadResponse =
        uploadRepository.uploadPhoto(part)
}
