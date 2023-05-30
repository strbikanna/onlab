package com.example.languagelearningapp.ui.screens.practice_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
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


