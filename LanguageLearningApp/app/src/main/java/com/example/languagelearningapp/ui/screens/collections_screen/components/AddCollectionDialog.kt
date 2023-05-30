package com.example.languagelearningapp.ui.screens.collections_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.languagelearningapp.R
import com.example.languagelearningapp.model.StudyCollection

@Composable
fun AddCollectionDialog(
    onAdd: (StudyCollection) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var collection by remember { mutableStateOf(StudyCollection(name = "")) }
    var invalidData by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Scaffold(
            topBar = {
                Text(
                    text = stringResource(R.string.add_collection),
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.h6
                )
            },
            bottomBar = {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary),
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Button(
                        onClick = {
                            if (collection.name.isNotBlank()) {
                                onAdd(collection)
                            } else {
                                invalidData = true
                            }
                        },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text(stringResource(R.string.Save))
                    }
                }
            },
            modifier = modifier
                .height(230.dp)
                .clip(MaterialTheme.shapes.medium)
                .padding(10.dp)
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(10.dp),
            ) {
                OutlinedTextField(
                    label = { Text(stringResource(R.string.coll_name)) },
                    value = collection.name,
                    onValueChange = { value ->
                        collection = collection.copy(name = value, favorite = false)
                        invalidData = false
                    },
                    isError = invalidData
                )
                if (invalidData) {
                    Text(
                        stringResource(R.string.add_coll_error_msg),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.overline,
                    )
                }
            }


        }
    }

}