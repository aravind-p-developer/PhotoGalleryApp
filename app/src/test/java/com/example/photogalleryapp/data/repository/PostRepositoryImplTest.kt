package com.example.photogalleryapp.data.repository

import com.example.photogalleryapp.data.remote.api.PostApiService
import com.example.photogalleryapp.data.remote.dto.CreatePostRequest
import com.example.photogalleryapp.data.remote.dto.PostDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Unit tests for [PostRepositoryImpl].
 */
class PostRepositoryImplTest {

    private lateinit var postApiService: PostApiService
    private lateinit var repository: PostRepositoryImpl

    @Before
    fun setup() {
        postApiService = mock()
        repository = PostRepositoryImpl(postApiService)
    }

    @Test
    fun `createPost success - returns mapped Post`() = runTest {
        // Given
        val responseDto = PostDto(id = 101, userId = 1, title = "Test Title", body = "Test Body")
        whenever(postApiService.createPost(any())).thenReturn(responseDto)

        // When
        val result = repository.createPost(userId = 1, title = "Test Title", body = "Test Body")

        // Then
        assertEquals(101, result.id)
        assertEquals(1, result.userId)
        assertEquals("Test Title", result.title)
        assertEquals("Test Body", result.body)
    }

    @Test
    fun `createPost maps all DTO fields to domain model`() = runTest {
        // Given
        val responseDto = PostDto(id = 999, userId = 5, title = "Another Title", body = "Another Body")
        whenever(postApiService.createPost(any())).thenReturn(responseDto)

        // When
        val result = repository.createPost(userId = 5, title = "Another Title", body = "Another Body")

        // Then
        assertEquals(999, result.id)
        assertEquals(5, result.userId)
        assertEquals("Another Title", result.title)
        assertEquals("Another Body", result.body)
    }
}
