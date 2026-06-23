package com.example.photogalleryapp.domain.repository

import com.example.photogalleryapp.domain.model.Post

/**
 * Repository interface for post operations.
 */
interface PostRepository {
    /** Submit a new post to the remote API. */
    suspend fun createPost(userId: Int, title: String, body: String): Post
}
