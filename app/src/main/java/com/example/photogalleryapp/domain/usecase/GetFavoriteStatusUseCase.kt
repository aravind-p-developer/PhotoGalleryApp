package com.example.photogalleryapp.domain.usecase

import com.example.photogalleryapp.domain.repository.PhotoRepository
import javax.inject.Inject

/**
 * Use case to check if a photo is currently marked as a favorite.
 */
class GetFavoriteStatusUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(photoId: Int): Boolean = photoRepository.isFavorite(photoId)
}
