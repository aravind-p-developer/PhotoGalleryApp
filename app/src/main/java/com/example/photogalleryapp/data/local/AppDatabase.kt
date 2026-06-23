package com.example.photogalleryapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.photogalleryapp.data.local.entity.FavoritePhotoEntity

/**
 * Room database for the app.
 * Currently holds a single table for favorite photos.
 */
@Database(
    entities = [FavoritePhotoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritePhotoDao(): FavoritePhotoDao
}
