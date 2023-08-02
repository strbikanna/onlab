package com.example.languagelearningapp.ui.screens.collections_detailed_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ActionsRow(
    favorite: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onFavorite: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var tappedFavorite by remember { mutableStateOf(favorite) }
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(onClick = { onDelete() }) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "delete",
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
        IconButton(onClick = { onEdit() }) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "edit",
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
        IconButton(onClick = {
            tappedFavorite = !tappedFavorite
            onFavorite(tappedFavorite)
        }) {
            var icon = Icons.Outlined.StarOutline
            if (tappedFavorite)
                icon = Icons.Default.Star
            Icon(
                icon,
                contentDescription = "favorite",
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
    }

}