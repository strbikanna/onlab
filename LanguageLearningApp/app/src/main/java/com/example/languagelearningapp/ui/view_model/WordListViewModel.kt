package com.example.languagelearningapp.ui.view_model


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagelearningapp.model.StudyCollection
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions
import com.example.languagelearningapp.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WordListViewModel @Inject constructor(
    private val wordRepo: WordRepository
) : ViewModel() {
    var word = Word(expression = "")
    private var _allWords: MutableStateFlow<List<WordWithDefinitions>> =
        MutableStateFlow(emptyList())

    var allWords: StateFlow<List<WordWithDefinitions>> = _allWords

    var openDialog by mutableStateOf(false)

    init {
        Log.v("VIEWMODEL", "entered initblock")
        viewModelScope.launch {
            Log.v("VIEWMODEL", "entered threadblock")
            _allWords.value = wordRepo.getAllWords()
            Log.v("VIEWMODEL", "initialized data")
        }
    }

    fun addWord(wordWithDefinitions: WordWithDefinitions) = viewModelScope.launch {
        wordRepo.addWord(wordWithDefinitions)
        reloadWords()
    }

    fun openDialog() {
        openDialog = true
    }

    fun closeDialog() {
        openDialog = false
    }
    private suspend fun reloadWords(){
            Log.v("VIEWMODEL", "reload words")
            _allWords.value=(wordRepo.getAllWords())
            Log.v("VIEWMODEL", "new data loaded")
    }
}