package com.example.photogalleryapp.domain.repository

import com.example.photogalleryapp.domain.model.Photo

/**
 * Repository interface for photo operations.
 * Abstracts the data source from the domain layer.
 */
interface PhotoRepository {
    /** Fetch all photos from the remote API. */
    suspend fun getPhotos(): List<Photo>

    /** Get a single photo by ID, merging with favorite status from local DB. */
    suspend fun getPhotoById(photoId: Int): Photo?

    /** Add a photo to favorites in the local database. */
    suspend fun addFavorite(photo: Photo)

    /** Remove a photo from favorites in the local database. */
    suspend fun removeFavorite(photoId: Int)

    /** Check if a photo is currently marked as a favorite. */
    suspend fun isFavorite(photoId: Int): Boolean

    /** Fetch all favorite photos from the local database. */
    suspend fun getFavoritePhotos(): List<Photo>
}
