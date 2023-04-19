package com.example.languagelearningapp.ui.screens.collectionsscreen.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddWordButton(
    openDialog: () -> Unit,
    modifier: Modifier = Modifier
){
    FloatingActionButton(
        onClick = openDialog,
        backgroundColor = MaterialTheme.colors.onPrimary
    ) {
        Icon(imageVector = Icons.Default.Add, "")
    }
}