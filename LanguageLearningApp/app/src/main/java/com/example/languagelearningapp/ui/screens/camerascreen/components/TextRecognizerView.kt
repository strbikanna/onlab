package com.example.languagelearningapp.ui.screens.camerascreen

import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import com.example.languagelearningapp.text_recognition.TextRecognizerViewModel
import com.example.languagelearningapp.ui.screens.camerascreen.components.WordHighLighterBox
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.internal.ImageConvertUtils


@Composable
@androidx.camera.core.ExperimentalGetImage
fun TextRecognizerView(
    image: ImageProxy,
    onBack: ()-> Unit,
    modifier: Modifier = Modifier,
    viewModel: TextRecognizerViewModel = TextRecognizerViewModel(),
) {
    if(image.image ==null) {
        return
    }
    val inputImage = InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees)
    viewModel.image = inputImage

    val recognizedText by viewModel.resultText.observeAsState()
    var imagePosition by remember { mutableStateOf(Offset.Zero) }
    var tapedPosition by remember { mutableStateOf(Offset.Zero)}
    var tapped by remember { mutableStateOf(false)}
    Card(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.background(Color.Black)
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.Default.ArrowBack, "")
            }
            Box {
                Image(
                    bitmap = getImageToShow(inputImage),
                    //ContentScale??
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { coordinates ->
                            imagePosition = coordinates.positionInRoot()
                            Log.d(
                                "TEXTVIEW",
                                "Image position at: ${imagePosition.x}, ${imagePosition.y}"
                            )
                        }
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                Log.d("TEXTVIEW", "Detected tap at: ${offset.x}, ${offset.y}")
                                tapedPosition = offset
                                tapped = true
                            }
                        }
                )
                if(tapped) {
                    Log.d("TEXTVIEW", "Trying to display tap position.")
                    val startPosition = Offset( tapedPosition.x, tapedPosition.y)
                    WordHighLighterBox(startPosition =startPosition, width = 150F, height =150F)
                }
            }
        }


    }


}

private fun getImageToShow(inputImage: InputImage) = ImageConvertUtils.getInstance().getUpRightBitmap(inputImage).asImageBitmap()

