package com.example.languagelearningapp.ui.screens.collections_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.R
import com.example.languagelearningapp.model.StudyCollection
import com.example.languagelearningapp.translation.Language
import com.example.languagelearningapp.translation.TranslatorViewModel
import com.example.languagelearningapp.ui.common.Dropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCollectionDialog(
    titleId: Int,
    onAdd: (StudyCollection) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialCollection: StudyCollection = StudyCollection(name = ""),
    viewModel: TranslatorViewModel = hiltViewModel()
) {
    var collection by remember { mutableStateOf(initialCollection) }
    var invalidData by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Scaffold(
            topBar = {
                Text(
                    text = stringResource(titleId),
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            bottomBar = {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
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
                .heightIn(max = 270.dp)
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
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Text(
                    stringResource(R.string.study_set_languages),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Dropdown(
                        onChosen = { chosen ->
                            val chosenLang =
                                viewModel.availableLanguages.find { lang -> lang.displayName == chosen }
                            chosenLang?.let {
                                viewModel.sourceLanguage = it
                                collection =
                                    collection.copy(sourceLanguage = Language(it.languageCode))
                            }

                        },
                        options = viewModel.availableLanguages.map { it.displayName },
                        label = stringResource(R.string.sourceLanguageLabel) + collection.sourceLanguage.displayName,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 3.dp)
                    )
                    Dropdown(
                        onChosen = { chosen ->
                            val chosenLang =
                                viewModel.availableLanguages.find { lang -> lang.displayName == chosen }
                            chosenLang?.let {
                                viewModel.targetLanguage = it
                                collection =
                                    collection.copy(targetLanguage = Language(it.languageCode))
                            }
                        },
                        options = viewModel.availableLanguages.map { it.displayName },
                        label = stringResource(R.string.targetLanguageLabel) + collection.targetLanguage.displayName,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 2.dp)
                    )
                }
            }


        }
    }

}