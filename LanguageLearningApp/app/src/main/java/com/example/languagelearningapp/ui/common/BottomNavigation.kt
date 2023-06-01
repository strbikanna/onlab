package com.example.languagelearningapp.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.languagelearningapp.R
import com.example.languagelearningapp.navigation.Screen

@Composable
fun BottomNavigationBar(
    onClickHome: () -> Unit,
    onClickCamera: () -> Unit,
    onClickCollections: () -> Unit,
    onClickDocuments: () -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(icon = {
            Icon(imageVector = Icons.Default.Home, "")
        },
            label = { Text(text = stringResource(R.string.HomeIconText)) },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.HomeScreen.route } == true,
            onClick = {
                onClickHome()
            },
            modifier = Modifier.weight(1f)
        )

        NavigationBarItem(icon = {
            Icon(imageVector = Icons.Default.Camera, "")
        },
            label = { Text(text = stringResource(R.string.PhotoIconText)) },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.CameraScreen.route } == true,
            onClick = {
                onClickCamera()
            },
            modifier = Modifier.weight(1f)
        )

        NavigationBarItem(icon = {
            Icon(imageVector = Icons.Default.DocumentScanner, "")
        },
            label = { Text(text = stringResource(R.string.DocumentsIconText)) },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.DocumentScreen.route } == true,
            onClick = {
                onClickDocuments()
            },
            modifier = Modifier.weight(1f)
        )
        NavigationBarItem(icon = {
            Icon(imageVector = Icons.Default.Folder, "")
        },
            label = { Text(text = stringResource(R.string.StudySetIconText)) },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.CollectionScreen.route } == true,
            onClick = {
                onClickCollections()
            },
            modifier = Modifier.weight(1f)
        )
    }
}

