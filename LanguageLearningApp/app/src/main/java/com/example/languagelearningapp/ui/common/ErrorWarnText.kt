package com.example.languagelearningapp.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorWarnText(onRetry: () -> Unit, message: String) {
    var isVisible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(message)
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.End)
            ) {
                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = { isVisible = false }
                ) {
                    Text(text = "Dismiss")
                }
                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = { onRetry() }
                ) {
                    Text(text = "Retry capture")
                }
            }
            Divider()
        }
    }
}