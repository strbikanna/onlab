package com.example.languagelearningapp.ui.view_model


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagelearningapp.dao.DefinitionDao
import com.example.languagelearningapp.dao.WordDao
import com.example.languagelearningapp.dao.WordDefinitionCrossRefDao
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
import kotlin.concurrent.thread


@HiltViewModel
class WordListViewModel @Inject constructor(
    private val wordRepo : WordRepository
) : ViewModel(){
    var word = Word(expression = "")
    private var _allWords: MutableStateFlow<List<WordWithDefinitions>> = MutableStateFlow(emptyList())
    var allWords : Flow<List<WordWithDefinitions>> = _allWords.asStateFlow()

    var openDialog by mutableStateOf(false)

    init{
        Log.v("VIEWMODEL", "entered initblock")
        thread{
            Log.v("VIEWMODEL", "entered threadblock")
            _allWords = MutableStateFlow(wordRepo.getAllWords())
            allWords = _allWords.asStateFlow()
            Log.v("VIEWMODEL", "initialized data")
        }
    }

    fun getWordById(id: Long) = viewModelScope.launch {
        word = wordRepo.getWordById(id)
    }
//TODO not on ui thread
    fun addWord(wordWithDefinitions: WordWithDefinitions) = viewModelScope.launch {
        wordRepo.addWord(wordWithDefinitions)
    }

    fun updateWord(word: Word) = viewModelScope.launch {
        wordRepo.updateWord(word)
    }
    fun updateWord(word: WordWithDefinitions) = viewModelScope.launch {
        wordRepo.updateWord(word)
    }

    fun deleteWord(word: Word) = viewModelScope.launch {
        wordRepo.deleteWord(word)
    }
    fun openDialog(){
        openDialog = true
    }
    fun closeDialog(){
        openDialog = false
    }
}