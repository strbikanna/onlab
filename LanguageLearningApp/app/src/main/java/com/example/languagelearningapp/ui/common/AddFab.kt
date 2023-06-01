package com.example.languagelearningapp.ui.common

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddButton(
    openDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = openDialog,
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Default.Add, "")
    }
}