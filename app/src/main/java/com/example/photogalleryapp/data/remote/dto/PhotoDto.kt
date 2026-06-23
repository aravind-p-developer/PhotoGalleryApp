package com.example.photogalleryapp.data.remote.dto

import com.google.gson.annotations.SerializedName

/** Data Transfer Object for Photo from the remote API. */
data class PhotoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("albumId") val albumId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String
)
