package com.example.photogalleryapp.data.repository

import com.example.photogalleryapp.data.local.FavoritePhotoDao
import com.example.photogalleryapp.data.remote.api.PhotoApiService
import com.example.photogalleryapp.data.remote.dto.PhotoDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Unit tests for [PhotoRepositoryImpl].
 * Tests the repository layer in isolation using mocked API and DAO.
 */
class PhotoRepositoryImplTest {

    private lateinit var photoApiService: PhotoApiService
    private lateinit var favoritePhotoDao: FavoritePhotoDao
    private lateinit var repository: PhotoRepositoryImpl

    @Before
    fun setup() {
        photoApiService = mock()
        favoritePhotoDao = mock()
        repository = PhotoRepositoryImpl(photoApiService, favoritePhotoDao)
    }

    @Test
    fun `getPhotos success - returns mapped Photo list`() = runTest {
        // Given
        val dtos = listOf(
            PhotoDto(id = 1, albumId = 1, title = "Test Photo", url = "http://url.com/1", thumbnailUrl = "http://thumb.com/1"),
            PhotoDto(id = 2, albumId = 1, title = "Another Photo", url = "http://url.com/2", thumbnailUrl = "http://thumb.com/2")
        )
        whenever(photoApiService.getPhotos()).thenReturn(dtos)
        whenever(favoritePhotoDao.getAllFavoriteIds()).thenReturn(listOf(2))

        // When
        val result = repository.getPhotos()

        // Then
        assertEquals(2, result.size)
        assertEquals("Test Photo", result[0].title)
        assertFalse(result[0].isFavorite)
        assertTrue(result[1].isFavorite)
    }

    @Test
    fun `getPhotos success - maps DTO fields correctly`() = runTest {
        // Given
        val dto = PhotoDto(id = 42, albumId = 5, title = "My Photo", url = "http://url", thumbnailUrl = "http://thumb")
        whenever(photoApiService.getPhotos()).thenReturn(listOf(dto))
        whenever(favoritePhotoDao.getAllFavoriteIds()).thenReturn(emptyList())

        // When
        val result = repository.getPhotos()

        // Then
        assertEquals(42, result[0].id)
        assertEquals(5, result[0].albumId)
        assertEquals("My Photo", result[0].title)
        assertEquals("http://url", result[0].url)
        assertEquals("http://thumb", result[0].thumbnailUrl)
    }

    @Test
    fun `isFavorite returns true when DAO count is greater than zero`() = runTest {
        // Given
        whenever(favoritePhotoDao.isFavorite(10)).thenReturn(1)

        // When
        val result = repository.isFavorite(10)

        // Then
        assertTrue(result)
    }

    @Test
    fun `isFavorite returns false when DAO count is zero`() = runTest {
        // Given
        whenever(favoritePhotoDao.isFavorite(99)).thenReturn(0)

        // When
        val result = repository.isFavorite(99)

        // Then
        assertFalse(result)
    }
}
