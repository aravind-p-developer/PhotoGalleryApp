package com.example.photogalleryapp.domain.model

/**
 * Domain model for a photo fetched from the API.
 * Pure Kotlin data class with no Android or framework dependencies.
 */
data class Photo(
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
    val isFavorite: Boolean = false
)
