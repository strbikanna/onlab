package com.example.languagelearningapp.ui.screens.text_recognizer_screen.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.google.mlkit.vision.text.Text.TextBlock

@Composable
fun TextVisualizer(
    blocks: List<TextBlock>,
    scaleFactorX: Float,
    scaleFactorY: Float,
    modifier: Modifier = Modifier,
    highlightColor: Color,
    onClick: (x: Float, y: Float) -> Unit
) {
    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    Log.d("CANVAS", "Detected tap at: ${offset.x}, ${offset.y}")
                    onClick(offset.x / scaleFactorX, offset.y / scaleFactorY)
                }
            },
    ) {
        blocks.forEach { block ->
            block.lines.forEach { line ->
                line.elements.forEach { element ->
                    val boundingBox = element.boundingBox
                    if (boundingBox != null) {
                        val topLeft = Offset(
                            boundingBox.left.toFloat() * scaleFactorX,
                            boundingBox.top.toFloat() * scaleFactorY
                        )
                        val size = Size(
                            boundingBox.width().toFloat() * scaleFactorX,
                            boundingBox.height().toFloat() * scaleFactorY
                        )
                        drawRect(
                            color = highlightColor,
                            topLeft = topLeft,
                            size = size,
                            style = Stroke(width = 5f)
                        )
                    }

                }

            }

        }

    }

}