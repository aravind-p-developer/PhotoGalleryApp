package com.example.photogalleryapp.presentation.photolist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.photogalleryapp.domain.model.Photo
import com.example.photogalleryapp.domain.usecase.GetPhotosUseCase
import com.example.photogalleryapp.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Unit tests for [PhotoListViewModel].
 */
class PhotoListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getPhotosUseCase: GetPhotosUseCase
    private lateinit var viewModel: PhotoListViewModel

    private val samplePhotos = listOf(
        Photo(id = 1, albumId = 1, title = "Sunset View", url = "url1", thumbnailUrl = "thumb1", isFavorite = false),
        Photo(id = 2, albumId = 1, title = "Mountain Peak", url = "url2", thumbnailUrl = "thumb2", isFavorite = false),
        Photo(id = 3, albumId = 2, title = "Ocean Waves", url = "url3", thumbnailUrl = "thumb3", isFavorite = false)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        getPhotosUseCase = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPhotos updates UI state to Success with data`() = runBlocking {
        whenever(getPhotosUseCase()).thenReturn(samplePhotos)
        viewModel = PhotoListViewModel(getPhotosUseCase)
        
        val job = launch(Dispatchers.Unconfined) { viewModel.uiState.collect {} }
        delay(400) // Wait for debounce(300)

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(3, (state as UiState.Success).data.size)
        job.cancel()
    }

    @Test
    fun `loadPhotos emits Loading state initially`() = runBlocking {
        whenever(getPhotosUseCase()).thenReturn(samplePhotos)
        viewModel = PhotoListViewModel(getPhotosUseCase)
        assertTrue(viewModel.uiState.value is UiState.Loading)
    }

    @Test
    fun `loadPhotos updates UI state to Error on exception`() = runBlocking {
        whenever(getPhotosUseCase()).thenThrow(RuntimeException("Network error"))
        viewModel = PhotoListViewModel(getPhotosUseCase)
        
        val job = launch(Dispatchers.Unconfined) { viewModel.uiState.collect {} }
        delay(400)

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
        assertEquals("Network error", (state as UiState.Error).message)
        job.cancel()
    }

    @Test
    fun `search query filters photos by title`() = runBlocking {
        whenever(getPhotosUseCase()).thenReturn(samplePhotos)
        viewModel = PhotoListViewModel(getPhotosUseCase)
        
        val job = launch(Dispatchers.Unconfined) { viewModel.uiState.collect {} }
        delay(400)

        viewModel.onSearchQueryChanged("mountain")
        delay(400)

        val state = viewModel.uiState.value as UiState.Success
        assertEquals(1, state.data.size)
        assertEquals("Mountain Peak", state.data[0].title)
        job.cancel()
    }

    @Test
    fun `search query is case-insensitive`() = runBlocking {
        whenever(getPhotosUseCase()).thenReturn(samplePhotos)
        viewModel = PhotoListViewModel(getPhotosUseCase)
        
        val job = launch(Dispatchers.Unconfined) { viewModel.uiState.collect {} }
        delay(400)

        viewModel.onSearchQueryChanged("OCEAN")
        delay(400)

        val state = viewModel.uiState.value as UiState.Success
        assertEquals(1, state.data.size)
        assertEquals("Ocean Waves", state.data[0].title)
        job.cancel()
    }

    @Test
    fun `empty search query returns all photos`() = runBlocking {
        whenever(getPhotosUseCase()).thenReturn(samplePhotos)
        viewModel = PhotoListViewModel(getPhotosUseCase)
        
        val job = launch(Dispatchers.Unconfined) { viewModel.uiState.collect {} }
        delay(400)

        viewModel.onSearchQueryChanged("mountain")
        delay(400)

        viewModel.onSearchQueryChanged("")
        delay(400)

        val state = viewModel.uiState.value as UiState.Success
        assertEquals(3, state.data.size)
        job.cancel()
    }
}
