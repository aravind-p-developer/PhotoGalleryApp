package com.example.photogalleryapp.domain.usecase

import com.example.photogalleryapp.domain.model.Photo
import com.example.photogalleryapp.domain.repository.PhotoRepository
import javax.inject.Inject

/**
 * Use case to fetch all photos from the repository.
 * Single-responsibility: only concerned with retrieving photos.
 */
class GetPhotosUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(): List<Photo> = photoRepository.getPhotos()
}
