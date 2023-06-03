package com.example.languagelearningapp.ui.screens.collections_detailed_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.model.Definition
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions
import com.example.languagelearningapp.ui.common.AddButton
import com.example.languagelearningapp.ui.common.AddWordToCollectionDialog
import com.example.languagelearningapp.ui.screens.collections_detailed_screen.components.StudySetTopAppBar
import com.example.languagelearningapp.ui.screens.collections_detailed_screen.components.SwipeableWordWithDefDetailedCard
import com.example.languagelearningapp.ui.view_model.WordCollectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySetDetailedScreen(
    studySetId: Long,
    bottomBar: @Composable () -> Unit,
    onBack: () -> Unit,
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            StudySetTopAppBar(
                title = collection?.name ?: "",
                onBackPressed = { onBack() },
                onFavorites = { favorite -> viewModel.filterFavoriteWords(favorite) },
                onPractice = { onPractice(collection?.collectionId!!) },
                onUnlearned = { learned -> viewModel.filterLearnedWords(learned) },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = { bottomBar() },
        floatingActionButton = { AddButton({ viewModel.openDialog() }) },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
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
    AddWordToCollectionDialog(
        openDialog = viewModel.openDialog,
        closeDialog = { viewModel.closeDialog() },
        initialWordWithDefinitions = wordToEdit,
        defaultCollection = collection
    )
}