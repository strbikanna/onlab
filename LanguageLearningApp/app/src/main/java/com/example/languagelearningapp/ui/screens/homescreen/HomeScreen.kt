package com.example.languagelearningapp.ui.screens.homescreen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.languagelearningapp.R
import com.example.languagelearningapp.ui.theme.LanguageLearningAppTheme
import com.example.languagelearningapp.ui.view_model.WordListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.languagelearningapp.ui.screens.homescreen.components.*

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: WordListViewModel = hiltViewModel()
) {
    Log.v("HOMESCREEN", "initializing...")
    val words by viewModel.allWords.collectAsState(
        initial = emptyList()
    )
    LanguageLearningAppTheme {
        Scaffold(
            topBar = {
                HomeHeader(
                    drawable = R.drawable.pinkandblue,
                    title = R.string.homePageTitle,
                    Modifier.heightIn(max = 120.dp)
                )
            },
            content = { paddingValues ->
                WordDefinitionCard(
                    words,
                    Modifier
                        .padding(paddingValues)
                        .padding(8.dp)
                )
            },
            floatingActionButton = { AddWordButton({ viewModel.openDialog() }) },
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomNavigationBar(
                    navController,
                    Modifier
                        .padding(8.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
            }
        )
        AddWordDialog(
            openDialog = viewModel.openDialog,
            closeDialog = { viewModel.closeDialog() },
            addWord = { word -> viewModel.addWord(word) }
        )
    }
}