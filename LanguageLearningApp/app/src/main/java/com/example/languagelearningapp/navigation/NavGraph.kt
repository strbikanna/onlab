package com.example.languagelearningapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.languagelearningapp.text_recognition.TextRecognizerScreen
import com.example.languagelearningapp.ui.screens.camerascreen.CameraScreen
import com.example.languagelearningapp.ui.screens.collectionsscreen.CollectionsScreen
import com.example.languagelearningapp.ui.screens.collectionsscreen.HeaderWithTitle
import com.example.languagelearningapp.ui.screens.collectionsscreen.StudySetDetailedScreen
import com.example.languagelearningapp.ui.screens.documentscreen.DocumentScreen
import com.example.languagelearningapp.ui.screens.homescreen.components.BottomNavigationBar
import com.example.languagelearningapp.ui.screens.practicescreen.PracticeScreen

const val COLL_ID = "collId"

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(
            route = Screen.HomeScreen.route
        ) {
            /*HomeScreen(
                bottomBar =
                {
                    BottomNavigationBar(
                        onClickHome = { onNavHome(navController) },
                        onClickCamera = { onNavCamera(navController) },
                        onClickCollections = { onNavCollections(navController) },
                        onClickDocuments = { onNavDocuments(navController) },
                        currentDestination = navController.currentDestination
                    )
                },
            )*/
            TextRecognizerScreen()
        }
        composable(
            route = Screen.CollectionScreen.route
        ) {
            CollectionsScreen(
                bottomBar =
                {
                    BottomNavigationBar(
                        onClickHome = { onNavHome(navController) },
                        onClickCamera = { onNavCamera(navController) },
                        onClickCollections = { onNavCollections(navController) },
                        onClickDocuments = { onNavDocuments(navController) },
                        currentDestination = navController.currentDestination
                    )
                },
                topBar = { title ->
                    HeaderWithTitle(title = title, onBackPressed = { navController.popBackStack() })
                },
                navigateDetailedCollection = { collId ->
                    navigateTo(navController, "${Screen.CollectionDetailedScreen.route}/${collId}")
                }
            )
        }
        composable(
            route = "${Screen.CollectionDetailedScreen.route}/{${COLL_ID}}",
            arguments = listOf(
                navArgument(name = COLL_ID) {
                    type = NavType.LongType
                    nullable = false
                }
            )
        ) { backstackEntry ->
            val id = backstackEntry.arguments?.getLong(COLL_ID) ?: 1
            StudySetDetailedScreen(
                studySetId = id,
                bottomBar = {
                    BottomNavigationBar(
                        onClickHome = { onNavHome(navController) },
                        onClickCamera = { onNavCamera(navController) },
                        onClickCollections = { onNavCollections(navController) },
                        onClickDocuments = { onNavDocuments(navController) },
                        currentDestination = navController.currentDestination
                    )
                },
                topBar = { title ->
                    HeaderWithTitle(title = title, onBackPressed = { navController.popBackStack() })
                }
            )
        }
        composable(
            route = Screen.CameraScreen.route
        ) {
            CameraScreen(navController)
        }
        composable(
            route = Screen.PracticeScreen.route
        ) {
            PracticeScreen(navController)
        }
        composable(
            route = Screen.DocumentScreen.route
        ) {
            DocumentScreen(navController)
        }
    }
}

private fun navigateTo(navController: NavController, destinationRoute: String) {
    navController.navigate(destinationRoute) {
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

private fun onNavHome(navController: NavController) {
    navigateTo(
        navController,
        Screen.HomeScreen.route
    )
}

private fun onNavCamera(navController: NavController) {
    navigateTo(
        navController,
        Screen.CameraScreen.route
    )
}

private fun onNavCollections(navController: NavController) {
    navigateTo(
        navController,
        Screen.CollectionScreen.route
    )
}

private fun onNavDocuments(navController: NavController) {
    navigateTo(
        navController,
        Screen.DocumentScreen.route
    )
}


