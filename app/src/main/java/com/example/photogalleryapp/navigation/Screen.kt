package com.example.photogalleryapp.navigation

/**
 * Sealed class representing all navigation routes in the app.
 * Using sealed classes ensures type safety and exhaustive when expressions.
 */
sealed class Screen(val route: String) {
    object PhotoList : Screen("photo_list")
    object Favorites : Screen("favorites")
    object PhotoDetail : Screen("photo_detail/{photoId}") {
        fun createRoute(photoId: Int) = "photo_detail/$photoId"
    }
    object CreatePost : Screen("create_post")
    object UploadPhoto : Screen("upload_photo")
}
