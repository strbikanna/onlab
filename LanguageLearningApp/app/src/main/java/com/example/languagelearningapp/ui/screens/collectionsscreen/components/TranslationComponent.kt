package com.example.languagelearningapp.ui.screens.collectionsscreen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.livedata.observeAsState
import com.example.languagelearningapp.R
import com.example.languagelearningapp.translation.TranslatorViewModel


@Composable
fun TranslationComponent(
    onTranslationResult: (String) -> Unit,
    inputText: String = "hello",
    translationViewModel: TranslatorViewModel = hiltViewModel()
) {
    var enabled by remember { mutableStateOf(false) }
    val translatedText by translationViewModel.translatedText.observeAsState()
    if (enabled) {
        Row {
            Dropdown(
                onChosen = { chosen ->
                    val chosenLang =
                        translationViewModel.availableLanguages.find { lang -> lang.displayName == chosen }
                    translationViewModel.sourceLanguage = chosenLang!!
                },
                options = translationViewModel.availableLanguages.map { it.displayName },
                label = stringResource(R.string.sourceLanguageLabel),
                modifier = Modifier.weight(1f)
            )
            Dropdown(
                onChosen = { chosen ->
                    val chosenLang =
                        translationViewModel.availableLanguages.find { lang -> lang.displayName == chosen }
                    translationViewModel.targetLanguage = chosenLang!!
                },
                options = translationViewModel.availableLanguages.map { it.displayName },
                label = stringResource(R.string.targetLanguageLabel),
                modifier = Modifier.weight(1f)
            )
        }

        TextField(
            value = translatedText?.result ?: "...",
            onValueChange = {},
            readOnly = true,
        )
        Button(onClick = { translationViewModel.translate(inputText) }) {
            Text(stringResource(R.string.translateButton))
        }
        onTranslationResult(translatedText?.result ?: "")

    } else {
        TextButton(onClick = { enabled = true }) {
            Text(text = stringResource(R.string.Google_translation))
        }
    }
}

