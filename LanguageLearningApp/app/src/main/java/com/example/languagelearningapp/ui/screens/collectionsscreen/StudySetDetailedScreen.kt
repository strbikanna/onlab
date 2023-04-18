package com.example.languagelearningapp.ui.screens.collectionsscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.languagelearningapp.model.StudyCollection
import com.example.languagelearningapp.ui.screens.collectionsscreen.components.SwipeableWordWithDefDetailedCard
import com.example.languagelearningapp.ui.screens.homescreen.components.BottomNavigationBar
import com.example.languagelearningapp.ui.view_model.WordCollectionViewModel

@Composable
fun StudySetDetailedScreen(
    studySetId: Long,
    bottomBar: @Composable ()->Unit,
    topBar: @Composable (title: String)->Unit,
    viewModel: WordCollectionViewModel = hiltViewModel()
) {
    viewModel.setUpForCollection(studySetId)
    val collection by viewModel.collection.observeAsState()
    val words by  viewModel.allWords.observeAsState()
    Scaffold(
        topBar = {topBar(collection?.name ?: "")},
        bottomBar = {bottomBar()}
    ) {
        padding ->
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(padding)){
            items(words ?: emptyList()){ wordWithDefinitions ->
                SwipeableWordWithDefDetailedCard(
                    wordWithDefinitions = wordWithDefinitions,
                    onDelete = {word -> viewModel.deleteWord(word.word)},
                    onEdit = {word -> viewModel.updateWord(word) /*TODO real update */},
                    onFavorite = {word, favorite ->
                        word.word = word.word.copy(favorite = favorite)
                        viewModel.updateWord(word.word)
                    }
                )
            }
        }
    }
}