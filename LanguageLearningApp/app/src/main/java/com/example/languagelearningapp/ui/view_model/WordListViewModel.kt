package com.example.languagelearningapp.ui.view_model


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions
import com.example.languagelearningapp.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    init {
        Log.v("VIEWMODEL", "entered initblock")
        viewModelScope.launch {
            Log.v("VIEWMODEL", "entered threadblock")
            _allWords.value = wordRepo.getAllWords()
            Log.v("VIEWMODEL", "initialized data")
        }
    }

}