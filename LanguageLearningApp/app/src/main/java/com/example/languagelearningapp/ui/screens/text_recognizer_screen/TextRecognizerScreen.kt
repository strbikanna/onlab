package com.example.languagelearningapp.ui.screens.text_recognizer_screen

import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.R
import com.example.languagelearningapp.model.CapturedImageCache
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions
import com.example.languagelearningapp.ui.common.AddWordToCollectionDialog
import com.example.languagelearningapp.ui.screens.camera_screen.components.ErrorWarnText
import com.example.languagelearningapp.ui.screens.text_recognizer_screen.components.ZoomableTextOnImage
import com.example.languagelearningapp.ui.view_model.TextRecognizerViewModel


@Composable
@androidx.camera.core.ExperimentalGetImage
fun TextRecognizerScreen(
    imageId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    textViewModel: TextRecognizerViewModel = hiltViewModel(),
) {
    val inputImage = CapturedImageCache.capturedImageHolder[imageId]
    if (inputImage == null) {
        onBack()
        return
    }
    textViewModel.image = inputImage
    Log.d("TEXTVIEW", "Inputimage size: ${inputImage.width}, ${inputImage.height}")

    val recognizedText by textViewModel.resultText.observeAsState()
    var chosenText by remember { mutableStateOf("") }
    var isNewTextChosen by remember { mutableStateOf(false) }

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
                    } else if (recognizedText?.text?.textBlocks?.size == 0) {
                        Text("No text elements are recognized on picture.")
                    } else {
                        Text("Tap to select recognized words")
                    }

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
            ZoomableTextOnImage(
                text = recognizedText?.text,
                image = textViewModel.getImageToShow(inputImage),
                onWordClicked = { position ->
                    chosenText = textViewModel.getWordByPosition(position)
                    isNewTextChosen = true
                },
            )
        }
    }
    val initialWord = WordWithDefinitions(
        word = Word(expression = chosenText),
        definitions = listOf()
    )
    AddWordToCollectionDialog(
        openDialog = isNewTextChosen,
        closeDialog = { isNewTextChosen = false },
        initialWordWithDefinitions = initialWord
    )
}






