package com.example.languagelearningapp.ui.screens.text_recognizer_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Colorize
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
        val color3 = MaterialTheme.colors.surface
        val buttonModifier = Modifier
            .size(40.dp)
        val buttonShape = CircleShape
        Button(
            onClick = {
                onColorChoose(color1)
            },
            modifier = buttonModifier,
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = color1)
        ) {}
        Button(
            onClick = {
                onColorChoose(color2)
            },
            modifier = buttonModifier,
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = color2)
        ) {}
        Button(
            onClick = {
                onColorChoose(color3)
            },
            modifier = buttonModifier,
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = color3)
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
        IconButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                Icons.Default.Colorize,
                "",
                modifier = Modifier.fillMaxSize()
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {
            HighlightColorChooser(
                onColorChoose = { color -> onColorChoose(color) },
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