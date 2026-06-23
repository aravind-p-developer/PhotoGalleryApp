package com.example.photogalleryapp.data.remote.api

import com.example.photogalleryapp.data.remote.dto.UploadResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/** Retrofit service for uploading images to httpbin. */
interface UploadApiService {
    @Multipart
    @POST("post")
    suspend fun uploadPhoto(@Part file: MultipartBody.Part): UploadResponseDto
}
