package com.example.languagelearningapp.ui.screens.camera_screen

import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.model.CapturedImageCache
import com.example.languagelearningapp.ui.permissions.CameraPermissionManager
import com.example.languagelearningapp.ui.view_model.CameraViewModel


private const val imageID = "pic0011"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
fun CameraScreen(
    bottomBar: @Composable () -> Unit,
    onBack: () -> Unit,
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

    LaunchedEffect(previewView) {
        viewModel.startCameraOnSurface(previewView, lifecycleOwner, context)
    }

    Scaffold(
        bottomBar = { bottomBar() },
        floatingActionButton = {
            IconButton(
                onClick = {
                    viewModel.capturePicture(context)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                ),
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    Icons.Outlined.Circle,
                    "",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.White
                )
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
        IconButton(
            onClick = {
                onBack()
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Filled.ArrowBack, "backIcon")
        }
    }
    if (captureSuccess == true && image != null) {
        CapturedImageCache.addImageProxy(imageID, image!!)
        viewModel.resetCaptureState()
        Log.d("CameraView", "Opening recognizer view.")
        onCaptureSuccess(imageID)
    }
}
