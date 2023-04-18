package com.example.languagelearningapp.ui.screens.collectionsscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.languagelearningapp.ui.screens.homescreen.components.BottomNavigationBar
import com.example.languagelearningapp.ui.view_model.WordCollectionViewModel


@Composable
fun CollectionsScreen(
    bottomBar: @Composable ()->Unit,
    topBar: @Composable (title: String)->Unit,
    navigateDetailedCollection: (collectionId: Long) -> Unit,
    viewModel: WordCollectionViewModel = hiltViewModel()
) {
    val studySets by viewModel.allCollections.collectAsState(
        initial = emptyList()
    )
    Scaffold(
        topBar = {
            topBar("Study Sets")
        },
        bottomBar = {
            bottomBar()
        }
    ) {
        padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(padding)
                .padding(top = 50.dp)
                .fillMaxWidth()
        ){
            items(studySets){ set ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    IconButton(
                        onClick = {
                                  navigateDetailedCollection(set.collectionId!!)
                                  },
                        modifier = Modifier.size(120.dp)
                    ) {
                        Icon(Icons.Default.Folder,
                            contentDescription = "studySet",
                            tint= MaterialTheme.colors.primaryVariant,
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
}