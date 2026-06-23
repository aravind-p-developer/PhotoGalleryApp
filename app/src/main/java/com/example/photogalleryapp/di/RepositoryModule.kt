package com.example.photogalleryapp.di

import com.example.photogalleryapp.data.repository.PhotoRepositoryImpl
import com.example.photogalleryapp.data.repository.PostRepositoryImpl
import com.example.photogalleryapp.data.repository.UploadRepositoryImpl
import com.example.photogalleryapp.domain.repository.PhotoRepository
import com.example.photogalleryapp.domain.repository.PostRepository
import com.example.photogalleryapp.domain.repository.UploadRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that binds repository interfaces to their implementations.
 * Uses @Binds for better performance (no reflection overhead vs @Provides).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPhotoRepository(impl: PhotoRepositoryImpl): PhotoRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    abstract fun bindUploadRepository(impl: UploadRepositoryImpl): UploadRepository
}
