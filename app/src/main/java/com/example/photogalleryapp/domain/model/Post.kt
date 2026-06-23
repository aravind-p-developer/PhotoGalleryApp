package com.example.photogalleryapp.domain.model

/**
 * Domain model for a post created via the API.
 */
data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)
