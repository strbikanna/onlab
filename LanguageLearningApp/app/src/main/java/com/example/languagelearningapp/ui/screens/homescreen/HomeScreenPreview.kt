package com.example.languagelearningapp.ui.screens.homescreen

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.languagelearningapp.R
import com.example.languagelearningapp.model.Definition
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions
import com.example.languagelearningapp.ui.screens.homescreen.components.AddWordButton
import com.example.languagelearningapp.ui.screens.homescreen.components.BottomNavigationBar
import com.example.languagelearningapp.ui.screens.homescreen.components.HomeHeader
import com.example.languagelearningapp.ui.screens.homescreen.components.WordDefinitionCard
import com.example.languagelearningapp.ui.theme.LanguageLearningAppTheme

@Preview
@Composable
fun HomeScreenPreView(
) {
    LanguageLearningAppTheme {
        Scaffold(
            topBar = {
                HomeHeader(
                    drawable = R.drawable.pinkandblue,
                    title = R.string.homePageTitle,
                    Modifier.heightIn(max = 100.dp)
                )
            },
            content = { paddingValues ->
                WordDefinitionCard(
                    getMockData(),
                    Modifier
                        .padding(paddingValues)
                        .padding(8.dp)
                        .fillMaxHeight()
                )
            },
            floatingActionButton ={ AddWordButton(openDialog = {mockFunction()} )},
            isFloatingActionButtonDocked = true,
            bottomBar = { BottomNavigationBar(
                navController = rememberNavController(),
               modifier = Modifier
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.medium)) },
            modifier = Modifier.fillMaxSize()
        )
    }
}
private fun getMockData(): List<WordWithDefinitions> {
    return listOf(
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumolcs"), Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(Definition(description = "gyumi"))
        ),
        WordWithDefinitions(
            Word(expression = "alma"),
            listOf(
                Definition(description = "gyumolcs"),
                Definition(description = "gyumi"),
                Definition(description = "piros gyumi")
            )
        ),
    )
}
fun mockFunction(){}