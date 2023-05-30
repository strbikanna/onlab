package com.example.languagelearningapp.ui.screens.collections_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.model.Definition
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions
import com.example.languagelearningapp.ui.common.AddButton
import com.example.languagelearningapp.ui.common.AddWordToCollectionDialog
import com.example.languagelearningapp.ui.screens.collections_screen.components.StudySetUseCaseControls
import com.example.languagelearningapp.ui.screens.collections_screen.components.SwipeableWordWithDefDetailedCard
import com.example.languagelearningapp.ui.view_model.WordCollectionViewModel

@Composable
fun StudySetDetailedScreen(
    studySetId: Long,
    bottomBar: @Composable () -> Unit,
    topBar: @Composable (title: String) -> Unit,
    onPractice: (Long) -> Unit,
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
        floatingActionButton = { AddButton({ viewModel.openDialog() }) },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            StudySetUseCaseControls(
                onPractice = { onPractice(studySetId) },
                onFavorites = { /*TODO*/ },
                onUnLearned = { /*TODO*/ },
                modifier = Modifier.weight(1f)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.weight(9f)
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

    }
    /*AddWordDialog(
        openDialog = viewModel.openDialog,
        closeDialog = { viewModel.closeDialog() },
        addWord = { word -> viewModel.addWordToCollection(word, collection!!) },
        initialWordWithDefinitions = wordToEdit
    )*/
    AddWordToCollectionDialog(
        openDialog = viewModel.openDialog,
        closeDialog = { viewModel.closeDialog() },
        initialWordWithDefinitions = wordToEdit
    )
}