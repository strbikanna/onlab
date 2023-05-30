package com.example.languagelearningapp.ui.screens.document_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.R
import com.example.languagelearningapp.model.CapturedImageCache
import com.example.languagelearningapp.ui.view_model.ImagePersistViewModel

@Composable
fun DocumentScreen(
    bottomBar: @Composable () -> Unit,
    topBar: @Composable (title: String) -> Unit,
    onImageClick: (String) -> Unit,
    viewModel: ImagePersistViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Scaffold(
        topBar = { topBar(stringResource(R.string.doc_screen_title)) },
        bottomBar = { bottomBar() }
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
                    )
                }
            }
        }


    }
}