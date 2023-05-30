package com.example.languagelearningapp.ui.screens.text_recognizer_screen.components

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.ui.view_model.ImageTransformationViewModel
import com.google.mlkit.vision.text.Text

@Composable
fun ZoomableTextOnImage(
    text: Text?,
    image: ImageBitmap,
    onWordClicked: (position: Offset) -> Unit,
    viewModel: ImageTransformationViewModel = hiltViewModel()
) {
    var highlightColor by remember { mutableStateOf(Color.White) }
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = viewModel.getAllowedScale(zoomChange * scale)
        offset = viewModel.getAllowedOffset(scale, offset + offsetChange)
        viewModel.onTransformation(scale, offset)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .transformable(state = state)
                .wrapContentHeight()
        ) {
            ImageContent(
                image = image,
                viewModel = viewModel
            )
            if (text != null) {
                TextVisualizer(
                    blocks = text.textBlocks,
                    scaleFactorX = viewModel.scaleFactorX.value!!,
                    scaleFactorY = viewModel.scaleFactorY.value!!,
                    onClick = { x, y ->
                        onWordClicked(Offset(x, y))
                    },
                    highlightColor = highlightColor
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ColorPicker(
                onColorChoose = { color ->
                    highlightColor = color
                },
                modifier = Modifier
                    .weight(3f)
                    .height(50.dp)
                    .padding(start = 15.dp)
            )
            SaveImageButton(
                image = image.asAndroidBitmap(),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(end = 15.dp)
            )
        }

    }
}