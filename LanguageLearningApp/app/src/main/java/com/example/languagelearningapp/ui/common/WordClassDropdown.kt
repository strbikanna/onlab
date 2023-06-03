package com.example.languagelearningapp.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.languagelearningapp.R
import com.example.languagelearningapp.model.Word

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordClassDropdown(
    setWordClass: (Word.WordClass) -> Unit,
    initialWordClass: Word.WordClass? = null,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    val wordClasses = Word.WordClass.values()

    var selectedValue by remember { mutableStateOf(initialWordClass?.name ?: "") }

    val icon = if (isExpanded)
        Icons.Default.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = { selectedValue = it },
            modifier = Modifier
                .fillMaxWidth(),

            label = { Text(stringResource(R.string.wordClassLabel)) },
            trailingIcon = {
                Icon(icon, "",
                    Modifier.clickable { isExpanded = !isExpanded })
            },
            readOnly = true
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            wordClasses.forEach { wc ->
                DropdownMenuItem(onClick = {
                    selectedValue = wc.name
                    isExpanded = false
                    setWordClass(wc)
                }, text = {
                    Text(text = wc.name)
                })
            }
        }
    }
}