package com.example.photogalleryapp.data.remote.api

import com.example.photogalleryapp.data.remote.dto.PhotoDto
import retrofit2.http.GET

/** Retrofit service for fetching photos from JSONPlaceholder. */
interface PhotoApiService {
    @GET("photos")
    suspend fun getPhotos(): List<PhotoDto>
}
