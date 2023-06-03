package com.example.languagelearningapp.ui.screens.document_screen

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.R
import com.example.languagelearningapp.model.CapturedImageCache
import com.example.languagelearningapp.ui.common.CollapsingTopAppBar
import com.example.languagelearningapp.ui.common.reallyPerformHapticFeedback
import com.example.languagelearningapp.ui.view_model.ImagePersistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentScreen(
    bottomBar: @Composable () -> Unit,
    onBack: () -> Unit,
    onImageClick: (String) -> Unit,
    viewModel: ImagePersistViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val localView = LocalView.current
    var deleteRequest by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            CollapsingTopAppBar(
                title = stringResource(R.string.doc_screen_title),
                onBackPressed = { onBack() },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = { bottomBar() },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        if (viewModel.canUseThumbnail()) {
            val thumbnails = viewModel.loadAllThumbnails(context)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(padding)
                    .padding(10.dp)
            ) {
                items(thumbnails.keys.toList()) { uri ->
                    Image(
                        bitmap = thumbnails[uri]!!.asImageBitmap(),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(5.dp)
                            .size(150.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .clickable {
                                val key = CapturedImageCache.addUri(context, uri)
                                onImageClick(key)
                            }
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        deleteRequest = true
                                        localView.reallyPerformHapticFeedback(
                                            HapticFeedbackConstants.LONG_PRESS
                                        )
                                        viewModel.chosenImageUri = uri
                                    },
                                    onTap = {
                                        val key = CapturedImageCache.addUri(context, uri)
                                        onImageClick(key)
                                    }
                                )
                            }
                    )
                }
            }
        }
    }
    if (deleteRequest) {
        AlertDialog(
            onDismissRequest = { deleteRequest = false },
            title = { Text("Delete image?") },
            dismissButton = {
                OutlinedButton(onClick = { deleteRequest = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteImage(context, viewModel.chosenImageUri)
                        deleteRequest = false
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.delete))
                }
            }
        )
    }
}