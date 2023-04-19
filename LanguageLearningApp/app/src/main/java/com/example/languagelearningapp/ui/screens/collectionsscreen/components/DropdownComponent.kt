package com.example.languagelearningapp.ui.screens.collectionsscreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun Dropdown(
    onChosen: (String) -> Unit,
    options: List<String>,
    label: String = "",
    modifier: Modifier = Modifier
) {

    var isExpanded by remember { mutableStateOf(false) }

    var selectedValue by remember { mutableStateOf("") }

    val icon = if (isExpanded)
        Icons.Default.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = { selectedValue = it },
            //modifier = Modifier.fillMaxWidth(),

            label = { label },
            trailingIcon = {
                Icon(icon, "",
                    Modifier.clickable { isExpanded = !isExpanded })
            }
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            options.forEach { opt ->
                DropdownMenuItem(onClick = {
                    selectedValue = opt
                    isExpanded = false
                    onChosen(opt)
                }) {
                    Text(text = opt)
                }
            }

        }
    }
}