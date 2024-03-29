package com.example.languagelearningapp.ui.common

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderWithTitle(
    title: String,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = {
                onBackPressed()
            }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        modifier = modifier
    )
}