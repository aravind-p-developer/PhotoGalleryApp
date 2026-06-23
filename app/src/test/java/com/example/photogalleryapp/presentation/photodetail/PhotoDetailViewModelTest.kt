package com.example.photogalleryapp.presentation.photodetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.photogalleryapp.domain.model.Photo
import com.example.photogalleryapp.domain.usecase.GetFavoriteStatusUseCase
import com.example.photogalleryapp.domain.usecase.GetPhotoByIdUseCase
import com.example.photogalleryapp.domain.usecase.ToggleFavoriteUseCase
import com.example.photogalleryapp.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Unit tests for [PhotoDetailViewModel].
 * Tests photo loading and favorite toggle state updates.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PhotoDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getPhotoByIdUseCase: GetPhotoByIdUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var getFavoriteStatusUseCase: GetFavoriteStatusUseCase
    private lateinit var viewModel: PhotoDetailViewModel

    private val samplePhoto = Photo(
        id = 10, albumId = 1, title = "Test Photo", url = "url", thumbnailUrl = "thumb", isFavorite = false
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getPhotoByIdUseCase = mock()
        toggleFavoriteUseCase = mock()
        getFavoriteStatusUseCase = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPhoto success updates state to Success and sets favorite status`() = runTest {
        // Given
        whenever(getPhotoByIdUseCase(10)).thenReturn(samplePhoto)
        whenever(getFavoriteStatusUseCase(10)).thenReturn(true)
        viewModel = PhotoDetailViewModel(getPhotoByIdUseCase, toggleFavoriteUseCase, getFavoriteStatusUseCase)

        // When
        viewModel.loadPhoto(photoId = 10)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals("Test Photo", (state as UiState.Success).data.title)
    }

    @Test
    fun `loadPhoto updates isFavorite from database`() = runTest {
        // Given
        whenever(getPhotoByIdUseCase(10)).thenReturn(samplePhoto)
        whenever(getFavoriteStatusUseCase(10)).thenReturn(true)
        viewModel = PhotoDetailViewModel(getPhotoByIdUseCase, toggleFavoriteUseCase, getFavoriteStatusUseCase)

        // When
        viewModel.loadPhoto(photoId = 10)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.isFavorite.value)
    }

    @Test
    fun `toggleFavorite flips isFavorite state`() = runTest {
        // Given - photo is loaded and initially not a favorite
        whenever(getPhotoByIdUseCase(10)).thenReturn(samplePhoto)
        whenever(getFavoriteStatusUseCase(10)).thenReturn(false)
        viewModel = PhotoDetailViewModel(getPhotoByIdUseCase, toggleFavoriteUseCase, getFavoriteStatusUseCase)
        viewModel.loadPhoto(photoId = 10)
        advanceUntilIdle()
        assertFalse(viewModel.isFavorite.value)

        // When
        viewModel.toggleFavorite()
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.isFavorite.value)
    }

    @Test
    fun `loadPhoto sets Error state when photo is not found`() = runTest {
        // Given
        whenever(getPhotoByIdUseCase(999)).thenReturn(null)
        viewModel = PhotoDetailViewModel(getPhotoByIdUseCase, toggleFavoriteUseCase, getFavoriteStatusUseCase)

        // When
        viewModel.loadPhoto(photoId = 999)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is UiState.Error)
    }
}
