package com.example.languagelearningapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.languagelearningapp.ui.screens.camerascreen.CameraScreen
import com.example.languagelearningapp.ui.screens.collectionsscreen.CollectionsScreen
import com.example.languagelearningapp.ui.screens.documentscreen.DocumentScreen
import com.example.languagelearningapp.ui.screens.homescreen.HomeScreen
import com.example.languagelearningapp.ui.screens.practicescreen.PracticeScreen

@Composable
fun NavGraph(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ){
        composable(
            route = Screen.HomeScreen.route
        ){
            HomeScreen(navController)
        }
        composable(
            route = Screen.CollectionScreen.route
        ){
            CollectionsScreen(navController)
        }
        composable(
            route = Screen.CameraScreen.route
        ){
            CameraScreen(navController)
        }
        composable(
            route = Screen.PracticeScreen.route
        ){
            PracticeScreen(navController)
        }
        composable(
            route = Screen.DocumentScreen.route
        ){
            DocumentScreen(navController)
        }
    }
}
