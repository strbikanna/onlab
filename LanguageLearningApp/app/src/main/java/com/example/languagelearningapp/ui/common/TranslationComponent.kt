package com.example.languagelearningapp.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagelearningapp.R
import com.example.languagelearningapp.translation.Language
import com.example.languagelearningapp.translation.TranslatorViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationComponent(
    onTranslationResult: (String) -> Unit,
    inputText: String = "hello",
    sourceLanguage: Language,
    targetLanguage: Language?,
    translationViewModel: TranslatorViewModel = hiltViewModel()
) {
    translationViewModel.sourceLanguage = sourceLanguage
    targetLanguage?.let {
        translationViewModel.targetLanguage = it
    }
    var targetChosen by remember { mutableStateOf(targetLanguage != null) }
    var enabled by remember { mutableStateOf(false) }
    val translatedText by translationViewModel.translatedText.observeAsState()
    if (enabled) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(sourceLanguage.displayName)
            Icon(Icons.Default.ArrowRight, "")
            if (targetChosen) {
                Text(translationViewModel.targetLanguage.displayName)
            } else {
                Dropdown(
                    onChosen = { chosen ->
                        val chosenLang =
                            translationViewModel.availableLanguages.find { lang -> lang.displayName == chosen }
                        chosenLang?.let {
                            translationViewModel.targetLanguage = it
                        }
                    },
                    options = translationViewModel.availableLanguages.map { it.displayName },
                    label = stringResource(R.string.targetLanguageLabel),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 2.dp)
                )
            }

        }
        OutlinedTextField(
            value = translatedText?.result ?: "...",
            onValueChange = {},
            readOnly = true,
        )
        Button(onClick = { translationViewModel.translate(inputText) }) {
            Text(stringResource(R.string.translateButton))
        }
        onTranslationResult(translatedText?.result ?: "")

    } else {
        TextButton(
            onClick = { enabled = true },
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = stringResource(R.string.Google_translation))
        }
    }
}

