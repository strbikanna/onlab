package com.example.languagelearningapp.ui.view_model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.abs
import kotlin.math.sign

class ImageTransformationViewModel : ViewModel() {
    var scaleFactorX = MutableLiveData<Float>(1F)
    var scaleFactorY = MutableLiveData<Float>(1F)
    var zoomScale = MutableLiveData<Float>(1F)
    var zoomOffset = MutableLiveData<Offset>(Offset.Zero)
    var initialImageCenterPosition = MutableLiveData<Offset>()
    var initialImagePosition = Offset.Zero
    var imageSize = IntSize(0, 0)
    private val maxScale = 3F
    private val minScale = 1F
    fun onTransformation(scale: Float, transition: Offset) {
        zoomScale.value = scale
        zoomOffset.value = transition
    }

    fun getAllowedScale(requestedScale: Float): Float {
        return max(min(requestedScale, maxScale), minScale)
    }

    fun getAllowedOffset(allowedScale: Float, requestedOffset: Offset): Offset {
        if (allowedScale > 3f) return Offset.Zero
        val xMaxOffset = imageSize.width * (allowedScale - 1) / 2
        val yMaxOffset = imageSize.height * (allowedScale - 1) / 2
        return Offset(
            min(xMaxOffset, abs(requestedOffset.x)) * sign(requestedOffset.x),
            min(yMaxOffset, abs(requestedOffset.y)) * sign(requestedOffset.y)
        )
    }

}