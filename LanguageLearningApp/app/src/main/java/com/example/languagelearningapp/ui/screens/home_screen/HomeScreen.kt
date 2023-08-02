package com.example.languagelearningapp.ui.screens.home_screen


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.R
import com.example.languagelearningapp.ui.screens.home_screen.components.WordDefinitionCard
import com.example.languagelearningapp.ui.view_model.WordListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    bottomBar: @Composable () -> Unit,
    viewModel: WordListViewModel = hiltViewModel()
) {
    val words by viewModel.allWords.collectAsState(
        initial = emptyList()
    )
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.homePageTitle)) },
                scrollBehavior = scrollBehavior
            )
        },
        content = { paddingValues ->
            WordDefinitionCard(
                words,
                Modifier
                    .padding(paddingValues)
                    .padding(8.dp)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        },
        bottomBar = {
            bottomBar()
        }
    )

}