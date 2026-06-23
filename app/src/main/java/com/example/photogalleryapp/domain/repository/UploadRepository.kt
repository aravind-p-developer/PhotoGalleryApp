package com.example.photogalleryapp.domain.repository

import com.example.photogalleryapp.domain.model.UploadResponse
import okhttp3.MultipartBody

/**
 * Repository interface for photo upload operations.
 */
interface UploadRepository {
    /** Upload an image file using a multipart request. */
    suspend fun uploadPhoto(part: MultipartBody.Part): UploadResponse
}
