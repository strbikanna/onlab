package com.example.languagelearningapp.ui.screens.homescreen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.languagelearningapp.R
import com.example.languagelearningapp.navigation.Screen

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        elevation = 10.dp,
        contentColor = MaterialTheme.colors.primaryVariant,
        backgroundColor = MaterialTheme.colors.primary,
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Home, "")
        },
            label = { Text(text = stringResource(R.string.HomeIconText)) },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.HomeScreen.route } == true,
            onClick = {
                navigateTo(navController, Screen.HomeScreen.route)
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Camera, "")
        },
            label = { Text(text = stringResource(R.string.PhotoIconText)) },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.CameraScreen.route } == true,
            onClick = {
                navigateTo(navController, Screen.CameraScreen.route)
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.DocumentScanner, "")
        },
            label = { Text(text = stringResource(R.string.DocumentsIconText)) },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.DocumentScreen.route } == true,
            onClick = {
                navigateTo(navController, Screen.DocumentScreen.route)
            })
        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Folder, "")
        },
            label = { Text(text = stringResource(R.string.StudySetIconText)) },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.CollectionScreen.route } == true,
            onClick = {
                navigateTo(navController, Screen.CollectionScreen.route)
            })
        Spacer(modifier = Modifier.width(50.dp))
    }
}


private fun navigateTo(navController: NavController, destinationRoute: String) {
    navController.navigate(destinationRoute) {
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}