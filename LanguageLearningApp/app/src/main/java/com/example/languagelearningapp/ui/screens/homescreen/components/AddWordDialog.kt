package com.example.languagelearningapp.ui.screens.homescreen.components

import androidx.compose.foundation.clipScrollableContainer
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
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

/*TODO more definitions
*  and other fields */

@Composable
fun AddWordDialog (
    openDialog: Boolean,
    closeDialog: () -> Unit,
    addWord: (word: WordWithDefinitions) -> Unit
){
    if(!openDialog)
        return
    var word by remember {mutableStateOf(Word(expression=""))}
    var def by remember {mutableStateOf(Definition(description =""))}
    var definitions = remember {mutableStateListOf(Definition(description = ""))}
    val focusRequester = FocusRequester()
    var definitionCount by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = closeDialog,
        title = {
            Text(text = stringResource(R.string.AddWordDialogTitle),
                modifier = Modifier
                    .padding(5.dp)
                    .height(30.dp) )
        },
        text = {
            Column{
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
                TextField(
                    value = word.expression,
                    onValueChange ={ word= word.copy(expression = it)},
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
                    modifier = Modifier.height(16.dp)
                )
                TextField(
                    value = def.description,
                    onValueChange = { def = def.copy(description = it) },
                    placeholder = {
                        Text(text = stringResource(R.string.DescriptionPlaceHolder) )
                    },
                    modifier = Modifier.focusRequester(focusRequester)
                )
                LazyColumn(){
                    itemsIndexed(definitions){
                        i, def ->
                        TextField(
                            value = definitions[i].description,
                            onValueChange = {v-> definitions[i] = definitions[i].copy(description = v) },
                            placeholder = {
                                Text(text = stringResource(R.string.DescriptionPlaceHolder) )
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
            }
        },
        confirmButton = {
            Button(
                onClick= {
                    closeDialog()
                    val wordWithDef = WordWithDefinitions(word, definitions)
                    addWord(wordWithDef)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
            ){
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
        modifier = Modifier.clipScrollableContainer(Orientation.Vertical)
    )
}

@Preview
@Composable
fun alertPreview(){ LanguageLearningAppTheme() {
    AddWordDialog(openDialog = true, closeDialog = { /*TODO*/ }, addWord ={} )
}
}