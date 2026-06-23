package com.example.photogalleryapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class annotated with @HiltAndroidApp to enable Hilt dependency injection.
 */
@HiltAndroidApp
class PhotoGalleryApplication : Application()
