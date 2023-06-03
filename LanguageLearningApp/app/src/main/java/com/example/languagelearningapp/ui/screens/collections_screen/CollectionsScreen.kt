package com.example.languagelearningapp.ui.screens.collections_screen

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.R
import com.example.languagelearningapp.ui.common.AddButton
import com.example.languagelearningapp.ui.common.CollapsingTopAppBar
import com.example.languagelearningapp.ui.common.reallyPerformHapticFeedback
import com.example.languagelearningapp.ui.screens.collections_screen.components.AddCollectionDialog
import com.example.languagelearningapp.ui.view_model.WordCollectionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(
    bottomBar: @Composable () -> Unit,
    onBack: () -> Unit,
    navigateDetailedCollection: (collectionId: Long) -> Unit,
    viewModel: WordCollectionViewModel = hiltViewModel()
) {
    val studySets by viewModel.allCollections.collectAsState(
        initial = emptyList()
    )
    var studySetDeleteRequested by remember { mutableStateOf(false) }
    var studySetEditRequested by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val localView = LocalView.current
    Scaffold(
        topBar = {
            CollapsingTopAppBar(
                title = stringResource(R.string.study_sets_title),
                onBackPressed = { onBack() },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            bottomBar()
        },
        floatingActionButton = {
            AddButton(openDialog = { openDialog = true })
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(padding)
                .padding(top = 50.dp)
                .fillMaxWidth()
        ) {
            items(studySets) { set ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            navigateDetailedCollection(set.collectionId!!)
                        },
                        modifier = Modifier
                            .size(120.dp)

                    ) {
                        Icon(
                            Icons.Default.Folder,
                            contentDescription = "studySet",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = {
                                            studySetDeleteRequested = true
                                            viewModel.collection.value = set
                                            localView.reallyPerformHapticFeedback(
                                                HapticFeedbackConstants.LONG_PRESS
                                            )
                                        },
                                        onTap = {
                                            navigateDetailedCollection(set.collectionId!!)
                                        }
                                    )
                                }
                        )
                    }
                    Text(
                        text = set.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        studySetEditRequested = true
                                        viewModel.collection.value = set
                                        localView.reallyPerformHapticFeedback(
                                            HapticFeedbackConstants.LONG_PRESS
                                        )
                                    }
                                )
                            }
                    )
                }

            }
        }

    }
    if (openDialog) {
        AddCollectionDialog(
            titleId = R.string.add_collection,
            onAdd = { collection ->
                viewModel.addCollection(collection)
                openDialog = false
            }, onDismiss = { openDialog = false }
        )
    }
    if (studySetDeleteRequested) {
        AlertDialog(
            onDismissRequest = { studySetDeleteRequested = false },
            title = { Text("Delete ${viewModel.collection.value?.name}?") },
            dismissButton = {
                OutlinedButton(onClick = { studySetDeleteRequested = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteCollection()
                        studySetDeleteRequested = false
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.delete))
                }
            }
        )
    }
    if (studySetEditRequested) {
        AddCollectionDialog(
            titleId = R.string.edit_collection,
            onAdd = { collection ->
                viewModel.updateCollection(collection)
                studySetEditRequested = false
            },
            onDismiss = { studySetEditRequested = false },
            initialCollection = viewModel.collection.value!!
        )
    }
}