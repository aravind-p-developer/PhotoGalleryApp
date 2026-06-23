package com.example.photogalleryapp.data.repository

import com.example.photogalleryapp.data.remote.api.PostApiService
import com.example.photogalleryapp.data.remote.dto.CreatePostRequest
import com.example.photogalleryapp.domain.model.Post
import com.example.photogalleryapp.domain.repository.PostRepository
import javax.inject.Inject

/**
 * Implementation of [PostRepository] backed by the remote API.
 */
class PostRepositoryImpl @Inject constructor(
    private val postApiService: PostApiService
) : PostRepository {

    override suspend fun createPost(userId: Int, title: String, body: String): Post {
        val response = postApiService.createPost(
            CreatePostRequest(userId = userId, title = title, body = body)
        )
        return Post(
            id = response.id,
            userId = response.userId,
            title = response.title,
            body = response.body
        )
    }
}
