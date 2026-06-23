package com.example.photogalleryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.photogalleryapp.presentation.createpost.CreatePostScreen
import com.example.photogalleryapp.presentation.favorites.FavoritesScreen
import com.example.photogalleryapp.presentation.photodetail.PhotoDetailScreen
import com.example.photogalleryapp.presentation.photolist.PhotoListScreen
import com.example.photogalleryapp.presentation.uploadphoto.UploadPhotoScreen

/**
 * Root navigation graph for the app.
 * Accepts a [NavHostController] from the parent so the bottom nav bar
 * in MainActivity can share the same controller.
 */
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.PhotoList.route
    ) {
        composable(route = Screen.PhotoList.route) {
            PhotoListScreen(
                onPhotoClick = { photoId ->
                    navController.navigate(Screen.PhotoDetail.createRoute(photoId))
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onPhotoClick = { photoId ->
                    navController.navigate(Screen.PhotoDetail.createRoute(photoId))
                }
            )
        }

        composable(
            route = Screen.PhotoDetail.route,
            arguments = listOf(
                navArgument("photoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val photoId = backStackEntry.arguments?.getInt("photoId") ?: return@composable
            PhotoDetailScreen(
                photoId = photoId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.CreatePost.route) {
            CreatePostScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.UploadPhoto.route) {
            UploadPhotoScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
