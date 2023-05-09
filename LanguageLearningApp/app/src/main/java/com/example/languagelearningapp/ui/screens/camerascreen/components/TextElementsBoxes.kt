package com.example.languagelearningapp.ui.screens.camerascreen.components

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions
import com.example.languagelearningapp.ui.view_model.WordCollectionViewModel
import com.google.mlkit.vision.text.Text
import kotlin.math.roundToInt

@Composable
fun TextElementsBoxes(
    block: Text.TextBlock,
    scaleFactorX: Float,
    scaleFactorY: Float,
    viewModel: WordCollectionViewModel = hiltViewModel()
) {
    var selectedWord by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(false) }
    block.lines.forEach { line ->
        line.elements.forEach { element ->
            val bounds = element.boundingBox
            if (bounds != null) {
                WordHighLighterBox(
                    startPositionPx = Offset(
                        bounds.left.toFloat() * scaleFactorX,
                        bounds.top.toFloat() * scaleFactorY
                    ),
                    widthPx = (bounds.width() * scaleFactorX).roundToInt(),
                    heightPx = (bounds.height() * scaleFactorY).roundToInt(),
                    onClick = {
                        selectedWord = element.text
                        selected = true
                    }
                )
            }
        }
    }
    AddWordToCollectionDialog(
        openDialog = selected,
        closeDialog = { selected = false },
        initialWordWithDefinitions = WordWithDefinitions(
            Word(expression = selectedWord),
            emptyList()
        )
    )
}