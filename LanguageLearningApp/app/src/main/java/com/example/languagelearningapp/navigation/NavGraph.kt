package com.example.languagelearningapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.languagelearningapp.ui.screens.camerascreen.CameraScreen
import com.example.languagelearningapp.ui.screens.collectionsscreen.CollectionsScreen
import com.example.languagelearningapp.ui.screens.collectionsscreen.HeaderWithTitle
import com.example.languagelearningapp.ui.screens.collectionsscreen.StudySetDetailedScreen
import com.example.languagelearningapp.ui.screens.documentscreen.DocumentScreen
import com.example.languagelearningapp.ui.screens.homescreen.HomeScreen
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
            HomeScreen(
                bottomBar = {createBottomBar(navController)},
            )
        }
        composable(
            route = Screen.CollectionScreen.route
        ) {
            CollectionsScreen(
                bottomBar = {createBottomBar(navController)},
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
                bottomBar = {createBottomBar(navController)},
                topBar = { title ->
                    HeaderWithTitle(title = title, onBackPressed = { navController.popBackStack() })
                }
            )
        }
        composable(
            route = Screen.CameraScreen.route
        ) {
            CameraScreen(
                bottomBar = {createBottomBar(navController)},
                topBar = { title ->
                    HeaderWithTitle(title = title, onBackPressed = { navController.popBackStack() })
                })
        }

        composable(
            route = Screen.PracticeScreen.route
        ) {
            PracticeScreen(
                bottomBar = {createBottomBar(navController)},
                topBar = { title ->
                    HeaderWithTitle(title = title, onBackPressed = { navController.popBackStack() })
                }
            )
        }
        composable(
            route = Screen.DocumentScreen.route
        ) {
            DocumentScreen(
                bottomBar = {createBottomBar(navController)},
                topBar = { title ->
                    HeaderWithTitle(title = title, onBackPressed = { navController.popBackStack() })
                }
            )
        }
    }
}

@Composable
private fun createBottomBar(navController: NavHostController) =
    BottomNavigationBar(
        onClickHome = { onNavHome(navController) },
        onClickCamera = { onNavCamera(navController) },
        onClickCollections = { onNavCollections(navController) },
        onClickDocuments = { onNavDocuments(navController) },
        currentDestination = navController.currentDestination
    )


private fun navigateTo(navController: NavController, destinationRoute: String) {
    navController.navigate(destinationRoute) {
        //popUpTo(navController.graph.findStartDestination().id) { saveState = true }
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


