package com.example.languagelearningapp.ui.common

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.R
import com.example.languagelearningapp.model.Definition
import com.example.languagelearningapp.model.StudyCollection
import com.example.languagelearningapp.model.WordWithDefinitions
import com.example.languagelearningapp.translation.Language
import com.example.languagelearningapp.ui.view_model.WordCollectionViewModel
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.job


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordToCollectionDialog(
    openDialog: Boolean,
    closeDialog: () -> Unit,
    initialWordWithDefinitions: WordWithDefinitions,
    defaultCollection: StudyCollection? = null,
    sourceLanguage: Language = Language(TranslateLanguage.ENGLISH),
    viewModel: WordCollectionViewModel = hiltViewModel()
) {
    if (!openDialog)
        return
    var word by remember { mutableStateOf(initialWordWithDefinitions.word) }
    var selectedCollection: StudyCollection by remember {
        mutableStateOf(
            defaultCollection ?: StudyCollection(name = "")
        )
    }
    var translatedDefinition by remember { mutableStateOf(Definition(description = "")) }
    val definitions = remember { mutableStateListOf(Definition(description = "")) }.apply {
        clear()
        addAll(initialWordWithDefinitions.definitions)
    }
    val collections by viewModel.allCollections.collectAsState()
    val focusRequester = FocusRequester()
    var invalidData by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        coroutineContext.job.invokeOnCompletion {
            focusRequester.requestFocus()
        }
    }
    Dialog(
        onDismissRequest = closeDialog,
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .height(440.dp)
        ) {
            Scaffold(
                topBar = {
                    Text(
                        text = stringResource(R.string.AddWordDialogTitle),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                bottomBar = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        OutlinedButton(
                            onClick = closeDialog,
                            //colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.cancel)
                            )
                        }
                        Button(
                            onClick = {
                                definitions.add(translatedDefinition)
                                definitions.removeIf { def -> def.description.isEmpty() }
                                if (selectedCollection.name != "" && word.expression != "" && definitions.isNotEmpty()) {
                                    closeDialog()
                                    val wordWithDef =
                                        WordWithDefinitions(word, definitions.toList())
                                    viewModel.addWordToCollection(
                                        wordWithDef,
                                        selectedCollection
                                    )
                                } else {
                                    invalidData = true
                                }
                            },
                            //colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryVariant),
                            modifier = Modifier.padding(start = 15.dp, end = 15.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.Save),
                            )
                        }
                    }
                }
            ) { padding ->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .padding(padding)
                        .padding(10.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = word.expression,
                            onValueChange = { word = word.copy(expression = it) },
                            placeholder = {
                                Text(text = stringResource(R.string.AddNewWordPlaceHolder))
                            },
                            modifier = Modifier.focusRequester(focusRequester)
                        )
                        Text("Select word class: ", modifier = Modifier.padding(top = 15.dp))
                        WordClassDropdown(
                            setWordClass = { wc -> word = word.copy(wordClass = wc) },
                            initialWordClass = initialWordWithDefinitions.word.wordClass
                        )
                        if (defaultCollection == null) {
                            Text("Add to study set: ", modifier = Modifier.padding(top = 15.dp))
                            CollectionDropdown(
                                collections = collections,
                                onSelected = { selectedCollection = it }
                            )
                        }
                    }
                    itemsIndexed(definitions) { i, def ->
                        OutlinedTextField(
                            value = definitions[i].description,
                            onValueChange = { v ->
                                definitions[i] = definitions[i].copy(description = v)
                            },
                            placeholder = {
                                Text(text = stringResource(R.string.DescriptionPlaceHolder))
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    definitions.remove(def)
                                }) {
                                    Icon(Icons.Default.Close, "")
                                }
                            },
                            modifier = Modifier.focusRequester(focusRequester)
                        )
                    }
                    item {
                        TextButton(
                            onClick = {
                                definitions.add(Definition(description = ""))
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(stringResource(R.string.addDefinition))
                        }
                        TranslationComponent(
                            onTranslationResult = { translation ->
                                translatedDefinition =
                                    translatedDefinition.copy(description = translation)
                            },
                            inputText = word.expression,
                            sourceLanguage = defaultCollection?.sourceLanguage ?: sourceLanguage,
                            targetLanguage = defaultCollection?.targetLanguage
                        )
                    }
                }
                if (invalidData) {
                    Toast.makeText(
                        LocalContext.current,
                        stringResource(R.string.invalidDataToast),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionDropdown(
    collections: List<StudyCollection>,
    onSelected: (StudyCollection) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    var selectedValue by remember { mutableStateOf("") }

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

            label = { Text(stringResource(R.string.study_set)) },
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
            collections.forEach { coll ->
                DropdownMenuItem(onClick = {
                    selectedValue = coll.name
                    isExpanded = false
                    onSelected(coll)
                },
                    text = {
                        Text(text = coll.name)
                    })
            }
        }
    }
}
