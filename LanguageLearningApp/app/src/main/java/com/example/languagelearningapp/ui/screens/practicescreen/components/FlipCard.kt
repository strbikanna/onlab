package com.example.languagelearningapp.ui.screens.practicescreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.example.languagelearningapp.model.WordWithDefinitions

enum class TransitionState {
    Zero, Flip, Reverse
}

@Composable
fun FlipCard(
    wordWithDefinitions: WordWithDefinitions,
    initialTransitionState: MutableLiveData<TransitionState>,
    initialSwipeState: MutableLiveData<Boolean>,
    onTransition: (TransitionState) -> Unit,
    onSwiped: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transitionTrigger by initialTransitionState.observeAsState()
    var flipRotation by remember(transitionTrigger) { mutableStateOf(0f) }
    val swipeOffset by remember { mutableStateOf(0f) }
    val swipeTrigger by initialSwipeState.observeAsState()
    val animationSpec = tween<Float>(600, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))
    LaunchedEffect(key1 = transitionTrigger) {
        if (transitionTrigger == TransitionState.Flip) {
            animate(
                initialValue = 0f,
                targetValue = 180f,
                animationSpec = animationSpec
            ) { value: Float, _: Float ->
                flipRotation = value
            }
        }
        if (transitionTrigger == TransitionState.Reverse) {
            animate(
                initialValue = 180f,
                targetValue = 0f,
                animationSpec = animationSpec
            ) { value: Float, _: Float ->
                flipRotation = value
            }
        }

    }
    val flipModifier = modifier
        .graphicsLayer {
            rotationY = flipRotation
            cameraDistance = 8 * density
        }
        .offset { IntOffset(0, swipeOffset.toInt()) }
        .pointerInput(Unit) {
            detectVerticalDragGestures { change, dragAmount ->
                if (dragAmount < -6) {
                    onSwiped()
                }
            }
        }
        .clickable {
            if (transitionTrigger == TransitionState.Zero || transitionTrigger == TransitionState.Reverse) {
                onTransition(TransitionState.Flip)
            } else {
                onTransition(TransitionState.Reverse)
            }
        }
    if (flipRotation < 90f || transitionTrigger == TransitionState.Zero) {
        AnimatedVisibility(
            visible = swipeTrigger ?: true,
            enter = slideInHorizontally(initialOffsetX = { full -> full / 2 }),
            exit = slideOutVertically()
        ) {
            SimpleCard(
                wordWithDefinitions = wordWithDefinitions,
                showWord = true,
                modifier = flipModifier
            )
        }
    } else {
        AnimatedVisibility(
            visible = swipeTrigger ?: true,
            exit = slideOutVertically()
        ) {
            SimpleCard(
                wordWithDefinitions = wordWithDefinitions,
                showWord = false,
                modifier = flipModifier.graphicsLayer {
                    rotationY = 180f
                }

            )
        }

    }

}

@Composable
private fun SimpleCard(
    wordWithDefinitions: WordWithDefinitions,
    showWord: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            if (showWord) {
                Text(
                    text = wordWithDefinitions.word.expression,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Text(
                    text = wordWithDefinitions.word.wordClass?.name ?: "",
                    style = MaterialTheme.typography.body1
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val defNumber = wordWithDefinitions.definitions.size
                    itemsIndexed(wordWithDefinitions.definitions) { idx, def ->
                        Text(
                            text = def.description,
                            style = MaterialTheme.typography.h3,
                        )
                        if (defNumber > 1 && idx < defNumber - 1) {
                            Divider(
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }
        }

    }
}
