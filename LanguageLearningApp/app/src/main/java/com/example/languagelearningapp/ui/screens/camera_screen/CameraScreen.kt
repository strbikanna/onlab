package com.example.languagelearningapp.ui.screens.camera_screen

import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.model.CapturedImageCache
import com.example.languagelearningapp.ui.permissions.CameraPermissionManager
import com.example.languagelearningapp.ui.view_model.CameraViewModel


private const val imageID = "pic0011"


@Composable
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
fun CameraScreen(
    bottomBar: @Composable () -> Unit,
    topBar: @Composable (title: String) -> Unit,
    onCaptureSuccess: (String) -> Unit,
    viewModel: CameraViewModel = hiltViewModel()
) {
    CameraPermissionManager()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val image by viewModel.capturedImage.observeAsState()
    var previewView = remember {
        PreviewView(context)
    }
    val captureSuccess by viewModel.captureSuccess.observeAsState()
    var reloadRequested by remember { mutableStateOf(false) }

    LaunchedEffect(previewView) {
        viewModel.startCameraOnSurface(previewView, lifecycleOwner, context)
    }

    Scaffold(
        topBar = { topBar("Capture text") },
        bottomBar = { bottomBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.capturePicture(context)
                }) {
                Icon(Icons.Default.Circle, "")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier.fillMaxHeight()
    ) { padding ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            factory = { _context ->
                previewView = PreviewView(_context).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    scaleType = PreviewView.ScaleType.FILL_START
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
                previewView
            }
        )
    }
    if (captureSuccess == true && image != null) {
        CapturedImageCache.addImageProxy(imageID, image!!)
        viewModel.resetCaptureState()
        Log.d("CameraView", "Opening recognizer view.")
        onCaptureSuccess(imageID)
    }
}
