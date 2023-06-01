package com.example.languagelearningapp.ui.screens.text_recognizer_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HighlightColorChooser(
    modifier: Modifier = Modifier,
    onColorChoose: (Color) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val color1 = MaterialTheme.colors.primaryVariant
        val color2 = MaterialTheme.colors.secondaryVariant
        val color3 = MaterialTheme.colors.onSurface
        val buttonModifier = Modifier
            .size(40.dp)
        val buttonShape = CircleShape
        Button(
            onClick = {
                onColorChoose(color1)
            },
            modifier = buttonModifier,
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(containerColor = color1)
        ) {}
        Button(
            onClick = {
                onColorChoose(color2)
            },
            modifier = buttonModifier,
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(containerColor = color2)
        ) {}
        Button(
            onClick = {
                onColorChoose(color3)
            },
            modifier = buttonModifier,
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(containerColor = color3)
        ) {}

    }
}

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    onColorChoose: (Color) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            HighlightColorChooser(
                onColorChoose = { color -> onColorChoose(color) },
            )
        }
        OutlinedButton(
            onClick = { expanded = !expanded },
            //modifier = Modifier.fillMaxHeight()
        ) {
            Icon(
                Icons.Default.Colorize,
                "",
            )
        }
    }
}

@Preview
@Composable
fun colorPickerPreview() {
    MaterialTheme() {
        ColorPicker(onColorChoose = {})
    }
}