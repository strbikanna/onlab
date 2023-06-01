package com.example.languagelearningapp.ui.screens.collections_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.R
import com.example.languagelearningapp.ui.common.AddButton
import com.example.languagelearningapp.ui.common.CollapsingTopAppBar
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
    var openDialog by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            CollapsingTopAppBar(
                title = stringResource(R.string.doc_screen_title),
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
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
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
                        modifier = Modifier.size(120.dp)
                    ) {
                        Icon(
                            Icons.Default.Folder,
                            contentDescription = "studySet",
                            //tint = MaterialTheme.colors.primarySurface,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        text = set.name,
                        style = MaterialTheme.typography.body1
                    )
                }

            }
        }

    }
    if (openDialog) {
        AddCollectionDialog(
            onAdd = { collection ->
                viewModel.addCollection(collection)
                openDialog = false
            }, onDismiss = { openDialog = false }
        )
    }
}