package com.example.photogalleryapp.data.remote.dto

import com.google.gson.annotations.SerializedName

/** Data Transfer Object for Post from the remote API. */
data class PostDto(
    @SerializedName("id") val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)

/** Request body for creating a post. */
data class CreatePostRequest(
    @SerializedName("userId") val userId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)
