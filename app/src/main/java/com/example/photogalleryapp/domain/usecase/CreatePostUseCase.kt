package com.example.photogalleryapp.domain.usecase

import com.example.photogalleryapp.domain.model.Post
import com.example.photogalleryapp.domain.repository.PostRepository
import javax.inject.Inject

/**
 * Use case to create a new post via the API.
 * Validates input before delegating to the repository.
 */
class CreatePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(title: String, body: String): Post {
        require(title.isNotBlank()) { "Title cannot be blank" }
        require(body.isNotBlank()) { "Body cannot be blank" }
        // Using userId = 1 as a fixed value since the API is a mock
        return postRepository.createPost(userId = 1, title = title.trim(), body = body.trim())
    }
}
