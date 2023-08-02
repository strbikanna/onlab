package com.example.languagelearningapp.ui.screens.practice_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.R
import com.example.languagelearningapp.ui.screens.practice_screen.components.FlipCard
import com.example.languagelearningapp.ui.view_model.PracticeScreenViewModel
import kotlinx.coroutines.delay
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    bottomBar: @Composable () -> Unit,
    topBar: @Composable (title: String) -> Unit,
    collectionId: Long,
    viewModel: PracticeScreenViewModel = hiltViewModel()
) {
    viewModel.initForCollection(collectionId)
    val words by viewModel.allWords.observeAsState()
    var actualItemIndex by remember { mutableStateOf(0) }
    var swipeStateChanged by remember { mutableStateOf(false) }
    val allItemsNumber = words!!.size
    Scaffold(
        topBar = { topBar("Practice") },
        bottomBar = { bottomBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            Text(
                text = "${min(actualItemIndex + 1, allItemsNumber)}/$allItemsNumber",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(25.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    enabled = actualItemIndex > 0,
                    onClick = {
                        viewModel.resetRevealedState()
                        actualItemIndex--
                    }
                ) {
                    Icon(
                        Icons.Default.ArrowBackIos, "",
                        modifier = Modifier.height(50.dp)
                    )
                }
                if (viewModel.swipeState.value == false) {
                    LaunchedEffect(swipeStateChanged) {
                        delay(1000)
                        viewModel.resetSwipeState()
                        actualItemIndex++
                    }
                }
                if (words != null && words!!.isNotEmpty() && actualItemIndex < allItemsNumber && actualItemIndex >= 0) {
                    Box {
                        val scope = rememberCoroutineScope()
                        var sizeModifier = Modifier.size(250.dp)
                        if (swipeStateChanged)
                            sizeModifier = Modifier.size(251.dp)
                        Spacer(modifier = sizeModifier)
                        FlipCard(
                            wordWithDefinitions = words!![actualItemIndex],
                            initialTransitionState = viewModel.revealedState,
                            initialSwipeState = viewModel.swipeState,
                            onTransition = { state -> viewModel.revealedState.value = state },
                            modifier = sizeModifier,
                            onSwiped = {
                                viewModel.swipeState.value = false
                                val updatedWord = words!![actualItemIndex].word.copy(learned = true)
                                viewModel.updateWord(updatedWord)
                                swipeStateChanged = !swipeStateChanged
                            }
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.finish_practice),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 50.dp, vertical = 100.dp)
                    )
                }
                IconButton(
                    enabled = actualItemIndex < allItemsNumber,
                    onClick = {
                        viewModel.resetRevealedState()
                        actualItemIndex++
                    }
                ) {
                    Icon(Icons.Default.ArrowForwardIos, "", modifier = Modifier.height(50.dp))
                }
            }
            if (viewModel.swipeState.value == false) {
                Toast(
                    text = stringResource(id = R.string.learned_toast),
                    duration = 5000,
                )
            }
        }
    }
}

@Composable
private fun Toast(
    text: String,
    duration: Long,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(duration)
        visible = false
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Card(
            modifier = modifier,
            shape = MaterialTheme.shapes.small,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(10.dp)
            )
        }
    }

}
