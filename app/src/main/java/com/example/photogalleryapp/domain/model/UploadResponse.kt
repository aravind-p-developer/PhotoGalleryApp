package com.example.photogalleryapp.domain.model

/**
 * Domain model representing a summary of the upload response from the server.
 */
data class UploadResponse(
    val url: String,
    val contentType: String,
    val size: Long
)
