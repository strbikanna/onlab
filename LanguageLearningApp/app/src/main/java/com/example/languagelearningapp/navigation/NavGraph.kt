package com.example.languagelearningapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.languagelearningapp.ui.common.BottomNavigationBar
import com.example.languagelearningapp.ui.common.HeaderWithTitle
import com.example.languagelearningapp.ui.screens.camera_screen.CameraScreen
import com.example.languagelearningapp.ui.screens.collections_detailed_screen.StudySetDetailedScreen
import com.example.languagelearningapp.ui.screens.collections_screen.CollectionsScreen
import com.example.languagelearningapp.ui.screens.document_screen.DocumentScreen
import com.example.languagelearningapp.ui.screens.home_screen.HomeScreen
import com.example.languagelearningapp.ui.screens.practice_screen.PracticeScreen
import com.example.languagelearningapp.ui.screens.text_recognizer_screen.TextRecognizerScreen

const val COLL_ID = "collId"
private const val IMAGE_ID = "im_key"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@androidx.camera.core.ExperimentalGetImage
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
                bottomBar = { createBottomBar(navController) },
            )
        }
        composable(
            route = Screen.CollectionScreen.route
        ) {
            CollectionsScreen(
                bottomBar = { createBottomBar(navController) },
                onBack = { navController.popBackStack() },
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
                bottomBar = { createBottomBar(navController) },
                onBack = { navController.popBackStack() },
                onPractice = { collId ->
                    navigateTo(
                        navController,
                        "${Screen.PracticeScreen.route}/$collId"
                    )
                }
            )
        }
        composable(
            route = Screen.CameraScreen.route
        ) {
            CameraScreen(
                bottomBar = { createBottomBar(navController) },
                onBack = { navController.popBackStack() },
                onCaptureSuccess = { imageId ->
                    navigateTo(navController, "${Screen.TextRecognizerScreen.route}/$imageId")
                },
            )
        }
        composable(
            route = "${Screen.TextRecognizerScreen.route}/{$IMAGE_ID}",
            arguments = listOf(
                navArgument(name = IMAGE_ID) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        )
        { backstackEntry ->
            val imageUriInfo = backstackEntry.arguments?.getString(IMAGE_ID)
            TextRecognizerScreen(
                imageId = imageUriInfo!!,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Screen.PracticeScreen.route}/{$COLL_ID}",
            arguments = listOf(
                navArgument(name = COLL_ID) {
                    type = NavType.LongType
                    nullable = false
                }
            )
        ) { backstackEntry ->
            val id = backstackEntry.arguments?.getLong(COLL_ID) ?: 1
            PracticeScreen(
                bottomBar = { createBottomBar(navController) },
                topBar = { title ->
                    HeaderWithTitle(title = title, onBackPressed = { navController.popBackStack() })
                },
                collectionId = id
            )
        }
        composable(
            route = Screen.DocumentScreen.route
        ) {
            DocumentScreen(
                bottomBar = { createBottomBar(navController) },
                onBack = { navController.popBackStack() },
                onImageClick = { imageId ->
                    navigateTo(navController, "${Screen.TextRecognizerScreen.route}/$imageId")
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


