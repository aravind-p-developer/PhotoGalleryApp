package com.example.photogalleryapp.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Raw response from httpbin.org/post.
 * The 'files' map holds uploaded file references; 'headers' provides metadata.
 */
data class UploadResponseDto(
    @SerializedName("url") val url: String = "",
    @SerializedName("headers") val headers: Map<String, String> = emptyMap(),
    @SerializedName("files") val files: Map<String, String> = emptyMap()
)
