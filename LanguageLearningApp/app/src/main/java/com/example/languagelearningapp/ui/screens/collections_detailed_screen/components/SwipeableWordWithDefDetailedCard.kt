package com.example.languagelearningapp.ui.screens.collections_detailed_screen.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.languagelearningapp.model.Definition
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions
import java.util.*
import kotlin.math.roundToInt


private const val ANIMATION_DURATION = 500
private const val CARD_OFFSET = 500f
private const val MIN_DRAG_AMOUNT = 6

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
private fun WordWithDefDetailedCard(
    wordWithDefinitions: WordWithDefinitions,
    modifier: Modifier = Modifier
) {
    var isRevealed by remember { mutableStateOf(false) }
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(transitionState, "cardSwipe")
    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = {
            if (isRevealed) CARD_OFFSET else 0f
        },
    )
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .offset { (IntOffset(offsetTransition.roundToInt(), 0)) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount >= MIN_DRAG_AMOUNT) isRevealed = true
                    if (dragAmount < -MIN_DRAG_AMOUNT) isRevealed = false
                }
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = wordWithDefinitions.word.expression,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                if (expanded && wordWithDefinitions.word.wordClass != null) {
                    Text(
                        text = wordWithDefinitions.word.wordClass?.name?.lowercase() ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }

            if (!expanded) {
                Text(
                    text = wordWithDefinitions.definitions[0].description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .weight(1f)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(wordWithDefinitions.definitions) {
                        Text(
                            text = it.description,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .weight(1f)
                        )
                    }
                }
            }
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.align(Alignment.Top)
            ) {
                var icon = Icons.Default.KeyboardArrowDown
                if (expanded)
                    icon = Icons.Default.KeyboardArrowUp
                Icon(
                    icon,
                    contentDescription = "expand",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }

        }
    }
}

@Composable
fun SwipeableWordWithDefDetailedCard(
    wordWithDefinitions: WordWithDefinitions,
    onDelete: (WordWithDefinitions) -> Unit = {},
    onEdit: (WordWithDefinitions) -> Unit = {},
    onFavorite: (WordWithDefinitions, Boolean) -> Unit = { _1, _2 -> },
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        ActionsRow(
            favorite = wordWithDefinitions.word.favorite,
            onDelete = { onDelete(wordWithDefinitions) },
            onEdit = { onEdit(wordWithDefinitions) },
            onFavorite = { favorite -> onFavorite(wordWithDefinitions, favorite) },
            modifier = modifier.height(100.dp)
        )
        WordWithDefDetailedCard(
            wordWithDefinitions = wordWithDefinitions,
            modifier = modifier.height(100.dp)
        )
    }

}

@Preview
@Composable
private fun WordWithDefPreview() {
    val data = WordWithDefinitions(
        Word(expression = "TestWord", wordClass = Word.WordClass.NOUN, favorite = false),
        listOf(
            Definition(description = "Test description"),
            Definition(description = "Test description"),
            Definition(description = "Test description"),
            Definition(description = "Long long long long ............ test description"),
            Definition(description = "Test description"),
            Definition(description = "Test description"),
            Definition(description = "Test description"),
        )
    )
    SwipeableWordWithDefDetailedCard(wordWithDefinitions = data)
}
