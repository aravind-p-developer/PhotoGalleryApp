package com.example.photogalleryapp.domain.usecase

import com.example.photogalleryapp.domain.model.Photo
import com.example.photogalleryapp.domain.repository.PhotoRepository
import javax.inject.Inject

/**
 * Use case to retrieve all favorite photos.
 */
class GetFavoritePhotosUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(): List<Photo> {
        return photoRepository.getFavoritePhotos()
    }
}
