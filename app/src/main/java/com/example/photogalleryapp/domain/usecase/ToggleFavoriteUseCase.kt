package com.example.photogalleryapp.domain.usecase

import com.example.photogalleryapp.domain.model.Photo
import com.example.photogalleryapp.domain.repository.PhotoRepository
import javax.inject.Inject

/**
 * Use case to toggle a photo's favorite status.
 * Adds to favorites if not already saved, removes if already saved.
 */
class ToggleFavoriteUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(photo: Photo) {
        val isFav = photoRepository.isFavorite(photo.id)
        if (isFav) {
            photoRepository.removeFavorite(photo.id)
        } else {
            photoRepository.addFavorite(photo)
        }
    }
}
