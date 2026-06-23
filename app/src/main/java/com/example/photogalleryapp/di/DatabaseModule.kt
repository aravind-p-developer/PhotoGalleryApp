package com.example.photogalleryapp.di

import android.content.Context
import androidx.room.Room
import com.example.photogalleryapp.data.local.AppDatabase
import com.example.photogalleryapp.data.local.FavoritePhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "photo_gallery_db"
        ).build()

    @Provides
    @Singleton
    fun provideFavoritePhotoDao(database: AppDatabase): FavoritePhotoDao =
        database.favoritePhotoDao()
}
