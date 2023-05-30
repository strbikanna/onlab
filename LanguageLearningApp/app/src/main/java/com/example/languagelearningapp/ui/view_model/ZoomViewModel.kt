package com.example.languagelearningapp.ui.view_model

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlin.math.abs

class ZoomViewModel : ViewModel() {
    private var imageCenterPosition: Offset = Offset.Zero
    private var relativePositionToCenter: Offset = Offset.Zero
    fun calculateRelativePositionToCenter(position: Offset, imageCenter: Offset) {
        imageCenterPosition = imageCenter
        relativePositionToCenter = Offset(
            position.x - imageCenterPosition.x,
            position.y - imageCenterPosition.y
        )
    }

    fun calculateTranslation(scale: Float, translation: Offset): Offset {
        if (scale == 1f || abs(scale - 1f) < 0.000001) {
            return translation
        }
        imageCenterPosition = Offset(
            imageCenterPosition.x + translation.x,
            imageCenterPosition.y + translation.y
        )
        return translation
    }

    fun calculateScaledPosition(scale: Float): Offset {
        val upScaledRelativePosition = Offset(
            relativePositionToCenter.x * scale,
            relativePositionToCenter.y * scale
        )
        //relativePositionToCenter = upScaledRelativePosition
        return imageCenterPosition + upScaledRelativePosition
    }
}