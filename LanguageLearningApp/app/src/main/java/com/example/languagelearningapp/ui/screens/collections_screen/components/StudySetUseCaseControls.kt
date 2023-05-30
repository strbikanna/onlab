package com.example.languagelearningapp.ui.screens.collections_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.languagelearningapp.R

@Composable
fun StudySetUseCaseControls(
    onPractice: () -> Unit,
    onFavorites: () -> Unit,
    onUnLearned: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val paddingModifier = Modifier.padding(10.dp)
        item {
            Button(
                onClick = { onPractice() },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                modifier = paddingModifier
            ) {
                Column {
                    Icon(Icons.Default.School, "")
                    Text(text = stringResource(R.string.practice_words))
                }

            }
            Button(
                onClick = { onFavorites() },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                modifier = paddingModifier
            ) {
                Column {
                    Icon(Icons.Default.Star, "")
                    Text(text = stringResource(R.string.favorites_filter))
                }
            }
            Button(
                onClick = { onUnLearned() },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                modifier = paddingModifier
            ) {
                Column {
                    Icon(Icons.Default.Rule, "")
                    Text(text = stringResource(R.string.learned_filter))
                }
            }
        }

    }

}