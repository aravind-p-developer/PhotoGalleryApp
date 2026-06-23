package com.example.photogalleryapp.di

import com.example.photogalleryapp.data.remote.api.PhotoApiService
import com.example.photogalleryapp.data.remote.api.PostApiService
import com.example.photogalleryapp.data.remote.api.UploadApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val JSONPLACEHOLDER_BASE_URL = "https://jsonplaceholder.typicode.com/"
    private const val HTTPBIN_BASE_URL = "https://httpbin.org/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("jsonplaceholder")
    fun provideJsonPlaceholderRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(JSONPLACEHOLDER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("httpbin")
    fun provideHttpBinRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(HTTPBIN_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providePhotoApiService(@Named("jsonplaceholder") retrofit: Retrofit): PhotoApiService =
        retrofit.create(PhotoApiService::class.java)

    @Provides
    @Singleton
    fun providePostApiService(@Named("jsonplaceholder") retrofit: Retrofit): PostApiService =
        retrofit.create(PostApiService::class.java)

    @Provides
    @Singleton
    fun provideUploadApiService(@Named("httpbin") retrofit: Retrofit): UploadApiService =
        retrofit.create(UploadApiService::class.java)
}
