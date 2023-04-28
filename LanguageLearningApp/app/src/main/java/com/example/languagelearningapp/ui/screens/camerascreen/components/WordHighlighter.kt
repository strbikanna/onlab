package com.example.languagelearningapp.ui.screens.camerascreen.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import kotlin.math.roundToInt

@Composable
fun WordHighLighterBox(
    startPosition: Offset,
    width: Float,
    height: Float,
    onClick: () -> Unit = {}
){
    Canvas(modifier = Modifier.fillMaxSize()){
        drawRect(
            color = Color.White,
            topLeft = startPosition,
            size = Size(width, height)
        )
    }

}
fun Modifier.absolutePosition(
    position: Offset,
    width: Int,
    height: Int
) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    Log.d("TEXTVIEW", "Placing box at: ${position.x}, ${position.y}")
    layout(width, height) {
        placeable.place(position.x.roundToInt(), position.y.roundToInt())
    }
}