package com.example.photogalleryapp.data.repository

import com.example.photogalleryapp.data.remote.api.UploadApiService
import com.example.photogalleryapp.domain.model.UploadResponse
import com.example.photogalleryapp.domain.repository.UploadRepository
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Implementation of [UploadRepository] that uploads images to httpbin.org.
 */
class UploadRepositoryImpl @Inject constructor(
    private val uploadApiService: UploadApiService
) : UploadRepository {

    override suspend fun uploadPhoto(part: MultipartBody.Part): UploadResponse {
        val response = uploadApiService.uploadPhoto(part)
        val contentType = response.headers["Content-Type"] ?: "unknown"
        // httpbin echoes back the file content; use its size as upload size
        val size = response.files.values.firstOrNull()?.length?.toLong() ?: 0L
        return UploadResponse(
            url = response.url,
            contentType = contentType,
            size = size
        )
    }
}
