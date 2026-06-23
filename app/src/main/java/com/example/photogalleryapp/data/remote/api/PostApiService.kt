package com.example.photogalleryapp.data.remote.api

import com.example.photogalleryapp.data.remote.dto.CreatePostRequest
import com.example.photogalleryapp.data.remote.dto.PostDto
import retrofit2.http.Body
import retrofit2.http.POST

/** Retrofit service for creating posts on JSONPlaceholder. */
interface PostApiService {
    @POST("posts")
    suspend fun createPost(@Body request: CreatePostRequest): PostDto
}
