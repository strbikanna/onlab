package com.example.languagelearningapp.ui.screens.camerascreen.components

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

@Composable
fun WordHighLighterBox(
    startPositionPx: Offset,
    widthPx: Int,
    heightPx: Int,
    onClick: () -> Unit = {}
) {

    OutlinedButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .absolutePosition(
                startPositionPx,
                widthPx,
                heightPx
            )
            .width(Dp(widthPx.toFloat() / 1.6F / 1.6F))
            .height(Dp(heightPx.toFloat() / 1.6F / 1.6F)),

        ) {

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
        Log.d("TEXTVIEW", "Width, height: $width, $height")
        placeable.place(position.x.roundToInt(), position.y.roundToInt())
    }
}