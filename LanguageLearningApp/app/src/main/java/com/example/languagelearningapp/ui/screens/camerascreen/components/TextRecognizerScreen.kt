package com.example.languagelearningapp.ui.screens.camerascreen.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.stringResource
import com.example.languagelearningapp.R
import com.example.languagelearningapp.text_recognition.TextRecognizerViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.internal.ImageConvertUtils


@Composable
@androidx.camera.core.ExperimentalGetImage
fun TextRecognizerScreen(
    imageId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TextRecognizerViewModel = TextRecognizerViewModel(),
) {
    val image = CapturedImageCache.globalImageHolder[imageId]
    if (image == null || image.image == null) {
        Log.d("TextRecognizer", "Empty image, going back.")
        onBack()
    }
    val inputImage = InputImage.fromMediaImage(image!!.image!!, image.imageInfo.rotationDegrees)
    viewModel.image = inputImage
    Log.d("TEXTVIEW", "Inputimage size: ${inputImage.width}, ${inputImage.height}")

    val recognizedText by viewModel.resultText.observeAsState()
    var imagePosition by remember { mutableStateOf(Offset.Zero) }
    var tapedPosition by remember { mutableStateOf(Offset.Zero) }
    var tapped by remember { mutableStateOf(false) }
    var scaleFactorX = 1F
    var scaleFactorY = 1F
    Scaffold(
        topBar = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(
                        onClick = { onBack() }
                    ) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                    if (recognizedText?.text == null) {
                        Text("Processing image...")
                    } else (
                            Text("Tap to select recognized words")
                            )
                }
                if (recognizedText?.text == null) {
                    LinearProgressIndicator(Modifier.fillMaxWidth())
                }
            }
        },
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (recognizedText?.error != null) {
                ErrorWarnText(
                    message = stringResource(R.string.textRecognizerError),
                    onRetry = { onBack() }
                )
            }
            Box {
                Image(
                    bitmap = getImageToShow(inputImage),
                    contentScale = ContentScale.Fit,
                    contentDescription = "",
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            imagePosition = coordinates.positionInRoot()
                            val size = coordinates.size
                            scaleFactorY =
                                coordinates.size.height.toFloat() / inputImage.height.toFloat()
                            scaleFactorX =
                                coordinates.size.width.toFloat() / inputImage.width.toFloat()
                            Log.d(
                                "TEXTVIEW",
                                "Image position at: ${imagePosition.x}, ${imagePosition.y}"
                            )
                            Log.d(
                                "TEXTVIEW",
                                "Image size: ${size.width}, ${size.height}"
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
                recognizedText?.text?.textBlocks?.forEach {
                    TextElementsBoxes(
                        block = it,
                        scaleFactorX = scaleFactorX,
                        scaleFactorY = scaleFactorY
                    )
                }
            }
        }
    }
}

private fun getImageToShow(inputImage: InputImage): ImageBitmap {
    val bitmap = ImageConvertUtils.getInstance().getUpRightBitmap(inputImage).asImageBitmap()
    Log.d("TEXTVIEW", "Bitmap size: ${bitmap.width}, ${bitmap.height}")
    return bitmap
}




