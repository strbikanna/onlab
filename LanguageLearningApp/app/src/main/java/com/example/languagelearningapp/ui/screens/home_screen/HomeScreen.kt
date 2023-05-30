package com.example.languagelearningapp.ui.screens.home_screen


import android.util.Log
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.R
import com.example.languagelearningapp.ui.screens.home_screen.components.HomeHeader
import com.example.languagelearningapp.ui.screens.home_screen.components.WordDefinitionCard
import com.example.languagelearningapp.ui.theme.LanguageLearningAppTheme
import com.example.languagelearningapp.ui.view_model.WordListViewModel

@Composable
fun HomeScreen(
    bottomBar: @Composable () -> Unit,
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
            bottomBar = {
                bottomBar()
            }
        )
    }
}