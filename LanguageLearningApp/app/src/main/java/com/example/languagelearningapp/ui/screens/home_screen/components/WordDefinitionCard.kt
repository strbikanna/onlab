package com.example.languagelearningapp.ui.screens.home_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.languagelearningapp.model.WordWithDefinitions

@Composable
fun WordDefinitionRow(
    wordWithDefinitions: WordWithDefinitions,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.primary,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = wordWithDefinitions.word.expression,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            )
            Text(
                text = wordWithDefinitions.definitions[0].description,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
fun WordDefinitionCard(
    list: List<WordWithDefinitions>,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = 10.dp,
        modifier = modifier,
    ) {
        LazyColumn(modifier = Modifier) {
            items(list) { wordWithDefs ->
                WordDefinitionRow(wordWithDefs)
            }
        }
    }
}