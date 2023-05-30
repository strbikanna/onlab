package com.example.languagelearningapp.ui.screens.practice_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.languagelearningapp.model.WordWithDefinitions

@Composable
internal fun SimpleCard(
    wordWithDefinitions: WordWithDefinitions,
    showWord: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            if (showWord) {
                Text(
                    text = wordWithDefinitions.word.expression,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Text(
                    text = wordWithDefinitions.word.wordClass?.name ?: "",
                    style = MaterialTheme.typography.body1
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val defNumber = wordWithDefinitions.definitions.size
                    itemsIndexed(wordWithDefinitions.definitions) { idx, def ->
                        Text(
                            text = def.description,
                            style = MaterialTheme.typography.h3,
                        )
                        if (defNumber > 1 && idx < defNumber - 1) {
                            Divider(
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }
        }

    }
}