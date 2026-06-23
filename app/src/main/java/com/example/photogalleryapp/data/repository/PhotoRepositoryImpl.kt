package com.example.photogalleryapp.data.repository

import com.example.photogalleryapp.data.local.FavoritePhotoDao
import com.example.photogalleryapp.data.local.entity.FavoritePhotoEntity
import com.example.photogalleryapp.data.remote.api.PhotoApiService
import com.example.photogalleryapp.domain.model.Photo
import com.example.photogalleryapp.domain.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of [PhotoRepository] that coordinates between the remote API
 * and the local Room database for favorite status.
 */
class PhotoRepositoryImpl @Inject constructor(
    private val photoApiService: PhotoApiService,
    private val favoritePhotoDao: FavoritePhotoDao
) : PhotoRepository {

    private var cachedPhotos: MutableList<Photo>? = null

    override suspend fun getPhotos(): List<Photo> = withContext(Dispatchers.IO) {
        cachedPhotos?.let { return@withContext it }
        
        val dtos = photoApiService.getPhotos()
        val favoriteIds = favoritePhotoDao.getAllFavoriteIds().toSet()
        
        val mapped = dtos.map { dto ->
            Photo(
                id = dto.id,
                albumId = dto.albumId,
                title = dto.title,
                url = dto.url,
                thumbnailUrl = dto.thumbnailUrl,
                isFavorite = favoriteIds.contains(dto.id)
            )
        }
        
        cachedPhotos = mapped.toMutableList()
        return@withContext cachedPhotos!!
    }

    override suspend fun getPhotoById(photoId: Int): Photo? = withContext(Dispatchers.IO) {
        cachedPhotos?.find { it.id == photoId } ?: getPhotos().find { it.id == photoId }
    }

    override suspend fun addFavorite(photo: Photo) = withContext(Dispatchers.IO) {
        favoritePhotoDao.insertFavorite(
            FavoritePhotoEntity(
                photoId = photo.id,
                title = photo.title,
                imageUrl = photo.url,
                thumbnailUrl = photo.thumbnailUrl
            )
        )
        updateCacheFavoriteStatus(photo.id, true)
    }

    override suspend fun removeFavorite(photoId: Int) = withContext(Dispatchers.IO) {
        favoritePhotoDao.deleteFavorite(photoId)
        updateCacheFavoriteStatus(photoId, false)
    }

    override suspend fun isFavorite(photoId: Int): Boolean = withContext(Dispatchers.IO) {
        favoritePhotoDao.isFavorite(photoId) > 0
    }

    override suspend fun getFavoritePhotos(): List<Photo> = withContext(Dispatchers.IO) {
        favoritePhotoDao.getAllFavorites().map { entity ->
            Photo(
                id = entity.photoId,
                albumId = 0, // Mock albumId for favorites as it's not strictly needed for display
                title = entity.title,
                url = entity.imageUrl,
                thumbnailUrl = entity.thumbnailUrl,
                isFavorite = true
            )
        }
    }

    private fun updateCacheFavoriteStatus(photoId: Int, isFav: Boolean) {
        cachedPhotos?.let { cache ->
            val index = cache.indexOfFirst { it.id == photoId }
            if (index != -1) {
                cache[index] = cache[index].copy(isFavorite = isFav)
            }
        }
    }
}
