package com.example.languagelearningapp.ui.screens.text_recognizer_screen.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.ui.view_model.ImageTransformationViewModel

@Composable
fun ImageContent(
    image: ImageBitmap,
    viewModel: ImageTransformationViewModel = hiltViewModel(),
) {
    Image(
        bitmap = image,
        contentScale = ContentScale.Fit,
        contentDescription = "",
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                viewModel.initialImagePosition = coordinates.positionInRoot()
                viewModel.imageSize = coordinates.size
                viewModel.scaleFactorY.value =
                    coordinates.size.height.toFloat() / image.height.toFloat()
                viewModel.scaleFactorX.value =
                    coordinates.size.width.toFloat() / image.width.toFloat()
            }
    )

}