package com.example.languagelearningapp.ui.screens.collectionsscreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.languagelearningapp.R
import com.example.languagelearningapp.model.Definition
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions
import com.example.languagelearningapp.ui.theme.LanguageLearningAppTheme
import kotlinx.coroutines.job


@Composable
fun AddWordDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    addWord: (word: WordWithDefinitions) -> Unit,
    initialWordWithDefinitions: WordWithDefinitions
) {
    if (!openDialog)
        return
    var word by remember { mutableStateOf(initialWordWithDefinitions.word) }
    var translatedDefinition by remember { mutableStateOf(Definition(description = "")) }
    val definitions = remember { mutableStateListOf(Definition(description = "")) }
    definitions.clear()
    definitions.addAll(initialWordWithDefinitions.definitions)
    val focusRequester = FocusRequester()
    var definitionCount by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = closeDialog,
        title = {
            Text(
                text = stringResource(R.string.AddWordDialogTitle),
                modifier = Modifier
                    .padding(5.dp)
                    .height(30.dp)
            )
        },
        text = {
            Column {
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
                TextField(
                    value = word.expression,
                    onValueChange = { word = word.copy(expression = it) },
                    placeholder = {
                        Text(text = stringResource(R.string.AddNewWordPlaceHolder))
                    },
                    modifier = Modifier.focusRequester(focusRequester)
                )
                LaunchedEffect(Unit) {
                    coroutineContext.job.invokeOnCompletion {
                        focusRequester.requestFocus()
                    }
                }
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                Text("Select word class: ")
                WordClassDropdown(
                    setWordClass = { wc -> word = word.copy(wordClass = wc) },
                    modifier = Modifier
                        .height(10.dp)
                        .width(20.dp)
                )
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                LazyColumn {
                    itemsIndexed(definitions) { i, def ->
                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )
                        TextField(
                            value = definitions[i].description,
                            onValueChange = { v ->
                                definitions[i] = definitions[i].copy(description = v)
                            },
                            placeholder = {
                                Text(text = stringResource(R.string.DescriptionPlaceHolder))
                            },
                            modifier = Modifier.focusRequester(focusRequester)
                        )
                    }
                }
                TextButton(
                    onClick = {
                        definitionCount++
                        definitions.add(Definition(description = ""))
                    }
                ) {
                    Text(
                        text = stringResource(R.string.addDefinition)
                    )
                }
                TranslationComponent(
                    onTranslationResult = { translation ->
                        translatedDefinition = translatedDefinition.copy(description = translation)
                    },
                    inputText = word.expression
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    closeDialog()
                    definitions.add(translatedDefinition)
                    definitions.removeIf { def -> def.description.isEmpty() }
                    val wordWithDef = WordWithDefinitions(word, definitions.toList())
                    addWord(wordWithDef)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
            ) {
                Text(
                    text = stringResource(R.string.Save),
                )
            }
        },
        dismissButton = {
            Button(
                onClick = closeDialog,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ) {
                Text(
                    text = stringResource(R.string.cancel)
                )
            }
        },
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.fillMaxSize(2f)
    )
}

@Composable
fun WordClassDropdown(
    setWordClass: (Word.WordClass) -> Unit,
    modifier: Modifier = Modifier
) {

    var isExpanded by remember { mutableStateOf(false) }

    val wordClasses = Word.WordClass.values()

    var selectedValue by remember { mutableStateOf("") }

    val icon = if (isExpanded)
        Icons.Default.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = { selectedValue = it },
            modifier = Modifier
                .fillMaxWidth(),

            label = { Text(stringResource(R.string.wordClassLabel)) },
            trailingIcon = {
                Icon(icon, "",
                    Modifier.clickable { isExpanded = !isExpanded })
            }
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
                }) {
                    Text(text = wc.name)
                }
            }
        }
    }
}

@Preview
@Composable
fun AlertPreview() {
    LanguageLearningAppTheme {
        AddWordDialog(openDialog = true, closeDialog = { }, addWord = {}, WordWithDefinitions(
            word = Word(expression = ""),
            definitions = listOf(Definition(description = ""))
        )
        )
    }
}