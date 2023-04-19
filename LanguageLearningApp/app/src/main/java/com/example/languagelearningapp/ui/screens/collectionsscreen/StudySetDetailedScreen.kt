package com.example.languagelearningapp.ui.screens.collectionsscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.model.Definition
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions
import com.example.languagelearningapp.ui.screens.collectionsscreen.components.AddWordButton
import com.example.languagelearningapp.ui.screens.collectionsscreen.components.AddWordDialog
import com.example.languagelearningapp.ui.screens.collectionsscreen.components.SwipeableWordWithDefDetailedCard
import com.example.languagelearningapp.ui.view_model.WordCollectionViewModel

@Composable
fun StudySetDetailedScreen(
    studySetId: Long,
    bottomBar: @Composable () -> Unit,
    topBar: @Composable (title: String) -> Unit,
    viewModel: WordCollectionViewModel = hiltViewModel()
) {
    viewModel.setUpForCollection(studySetId)
    val collection by viewModel.collection.observeAsState()
    val words by viewModel.allWords.observeAsState()
    var wordToEdit by remember {
        mutableStateOf(
            WordWithDefinitions(
                word = Word(expression = ""),
                definitions = listOf(Definition(description = ""))
            )
        )
    }
    Scaffold(
        topBar = { topBar(collection?.name ?: "") },
        bottomBar = { bottomBar() },
        floatingActionButton = { AddWordButton({ viewModel.openDialog() }) },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            items(words ?: emptyList()) { wordWithDefinitions ->
                SwipeableWordWithDefDetailedCard(
                    wordWithDefinitions = wordWithDefinitions,
                    onDelete = { word -> viewModel.deleteWord(word.word) },
                    onEdit = { word ->
                        wordToEdit = word
                        viewModel.openDialog()
                    },
                    onFavorite = { word, favorite ->
                        word.word = word.word.copy(favorite = favorite)
                        viewModel.updateWord(word.word)
                    }
                )
            }
        }
    }
    AddWordDialog(
        openDialog = viewModel.openDialog,
        closeDialog = { viewModel.closeDialog() },
        addWord = { word -> viewModel.addWord(word) },
        initialWordWithDefinitions = wordToEdit
    )
}