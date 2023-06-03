package com.example.languagelearningapp.ui.screens.text_recognizer_screen.components

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.ui.view_model.ImagePersistViewModel
import java.io.IOException

@Composable
fun SaveImageButton(
    image: Bitmap,
    modifier: Modifier = Modifier,
    viewModel: ImagePersistViewModel = hiltViewModel()
) {
    var isSaved by remember { mutableStateOf(false) }
    val context = LocalContext.current
    FloatingActionButton(
        onClick = {
            if (!isSaved) {
                try {
                    viewModel.saveImage(context, image, "testImage")
                } catch (ex: IOException) {
                    Log.e("SaveImage", ex.message.toString())
                }
                isSaved = true
            }
        },
        modifier = modifier
    ) {
        Icon(
            Icons.Default.Save, "",
        )
    }

}