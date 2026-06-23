package com.example.photogalleryapp.domain.usecase

import com.example.photogalleryapp.domain.model.Photo
import com.example.photogalleryapp.domain.repository.PhotoRepository
import javax.inject.Inject

/**
 * Use case to fetch a single photo by its ID from the repository cache.
 */
class GetPhotoByIdUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(photoId: Int): Photo? {
        return photoRepository.getPhotoById(photoId)
    }
}
