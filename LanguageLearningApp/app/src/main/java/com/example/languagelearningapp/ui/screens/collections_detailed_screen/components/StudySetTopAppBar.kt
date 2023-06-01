package com.example.languagelearningapp.ui.screens.collections_detailed_screen.components

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySetTopAppBar(
    title: String,
    onBackPressed: () -> Unit,
    onFavorites: (Boolean) -> Unit,
    onPractice: () -> Unit,
    onUnlearned: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior
) {
    var favoritesPressed by remember { mutableStateOf(false) }
    var unlearnedPressed by remember { mutableStateOf(false) }
    MediumTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = {
                onBackPressed()
            }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        actions = {
            IconButton(
                onClick = {
                    favoritesPressed = !favoritesPressed
                    onFavorites(favoritesPressed)
                },
            ) {
                if (favoritesPressed) {
                    Icon(Icons.Default.Star, "")
                } else {
                    Icon(Icons.Default.StarOutline, "")
                }
            }
            IconButton(
                onClick = {
                    unlearnedPressed = !unlearnedPressed
                    onUnlearned(unlearnedPressed)
                },
            ) {
                if (unlearnedPressed) {
                    Icon(Icons.Default.DoneAll, "")
                } else {
                    Icon(Icons.Default.RemoveDone, "")
                }
            }
            IconButton(
                onClick = { onPractice() },
            ) {
                Icon(Icons.Default.School, "")
            }
        }
    )
}