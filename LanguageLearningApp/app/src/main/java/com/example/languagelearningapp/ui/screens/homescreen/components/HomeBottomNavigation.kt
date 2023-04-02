package com.example.languagelearningapp.ui.screens.homescreen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.languagelearningapp.R

@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    val selectedIndex = remember { mutableStateOf(0) }
    BottomNavigation(
        elevation = 10.dp,
        contentColor = MaterialTheme.colors.primaryVariant,
        backgroundColor = MaterialTheme.colors.primary,
        modifier = modifier
    ) {

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Home, "")
        },
            label = { Text(text = stringResource(R.string.HomeIconText)) },
            selected = (selectedIndex.value == 0),
            onClick = {
                selectedIndex.value = 0
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Camera, "")
        },
            label = { Text(text = stringResource(R.string.PhotoIconText)) },
            selected = (selectedIndex.value == 1),
            onClick = {
                selectedIndex.value = 1
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.DocumentScanner, "")
        },
            label = { Text(text = stringResource(R.string.DocumentsIconText)) },
            selected = (selectedIndex.value == 2),
            onClick = {
                selectedIndex.value = 2
            })
        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Folder, "")
        },
            label = { Text(text = stringResource(R.string.StudySetIconText)) },
            selected = (selectedIndex.value == 3),
            onClick = {
                selectedIndex.value = 3
            })
        Spacer(modifier = Modifier.width(30.dp))
    }
}