package com.example.photogalleryapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for storing favorite photos.
 * Persists across app restarts.
 */
@Entity(tableName = "favorite_photos")
data class FavoritePhotoEntity(
    @PrimaryKey val photoId: Int,
    val title: String,
    val imageUrl: String,
    val thumbnailUrl: String
)
