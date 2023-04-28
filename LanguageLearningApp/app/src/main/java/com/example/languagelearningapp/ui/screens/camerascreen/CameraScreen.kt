package com.example.languagelearningapp.ui.screens.camerascreen

import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lens
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.example.languagelearningapp.ui.permissions.CameraPermissionManager
import com.example.languagelearningapp.ui.view_model.CameraViewModel

@Composable
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
fun CameraScreen(
    bottomBar: @Composable () -> Unit,
    topBar: @Composable (title: String) -> Unit,
    viewModel: CameraViewModel = CameraViewModel()
) {
    CameraPermissionManager()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var captureRequested by remember{ mutableStateOf(false) }
    var capturedImage = viewModel.capturedImage.observeAsState()

    Scaffold(
        topBar = { topBar("Capture text") },
        bottomBar = { bottomBar() },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.capturePicture(context)
                captureRequested = true
            }) {
                Icon(Icons.Default.Lens, "")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier.fillMaxHeight()
    ) { padding ->

        val scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            factory = { context ->
                val previewView = PreviewView(context).apply {
                    this.scaleType = scaleType
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
                viewModel.startCameraOnSurface(
                    previewView,
                    lifecycleOwner,
                    context
                )

                previewView
            })
    }
    if(captureRequested && capturedImage.value != null){
        TextRecognizerView(image = capturedImage.value!!,
            onBack = {captureRequested = false},
            modifier = Modifier.fillMaxSize()
        )
 }

}
