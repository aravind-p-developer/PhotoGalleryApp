package com.example.photogalleryapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.photogalleryapp.data.local.entity.FavoritePhotoEntity

/** Room DAO for favorite photo operations. */
@Dao
interface FavoritePhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(entity: FavoritePhotoEntity)

    @Query("DELETE FROM favorite_photos WHERE photoId = :photoId")
    suspend fun deleteFavorite(photoId: Int)

    @Query("SELECT * FROM favorite_photos WHERE photoId = :photoId LIMIT 1")
    suspend fun getFavoriteById(photoId: Int): FavoritePhotoEntity?

    @Query("SELECT COUNT(*) FROM favorite_photos WHERE photoId = :photoId")
    suspend fun isFavorite(photoId: Int): Int

    @Query("SELECT photoId FROM favorite_photos")
    suspend fun getAllFavoriteIds(): List<Int>

    @Query("SELECT * FROM favorite_photos")
    suspend fun getAllFavorites(): List<FavoritePhotoEntity>
}
