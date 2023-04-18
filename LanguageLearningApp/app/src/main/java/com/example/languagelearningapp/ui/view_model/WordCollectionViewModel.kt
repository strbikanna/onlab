package com.example.languagelearningapp.ui.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagelearningapp.model.StudyCollection
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions
import com.example.languagelearningapp.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordCollectionViewModel @Inject constructor(
    private val repo: WordRepository
) : ViewModel() {

    var word = Word(expression = "")
    var collection = MutableLiveData(StudyCollection(name = ""))

    private var _allWords: MediatorLiveData<List<WordWithDefinitions>> =
        MediatorLiveData(emptyList())
    private var _allCollections: MutableStateFlow<List<StudyCollection>> =
        MutableStateFlow(emptyList())

    var allCollections: StateFlow<List<StudyCollection>> = _allCollections
    var allWords: MediatorLiveData<List<WordWithDefinitions>> = _allWords


    var openDialog by mutableStateOf(false)

    init {
        Log.v("VIEWMODEL", "entered initblock")
        viewModelScope.launch {
            Log.v("VIEWMODEL", "entered threadblock")
            _allCollections.value = repo.getAllCollections()
            Log.v("VIEWMODEL", "initialized data")
        }
        _allWords.addSource(collection
        ) {
            if(it.collectionId != null)
                getAllWordsInStudyCollection(it)
        }
    }
    fun setUpForCollection(id: Long){
        getCollectionById(id)
    }

    fun getWordById(id: Long) = viewModelScope.launch {
        word = repo.getWordById(id)
    }

    fun addWord(wordWithDefinitions: WordWithDefinitions) = viewModelScope.launch {
        repo.addWord(wordWithDefinitions)
        reloadWords()
    }

    fun updateWord(word: Word) = viewModelScope.launch {
        repo.updateWord(word)
        reloadWords()
    }

    fun updateWord(word: WordWithDefinitions) = viewModelScope.launch {
        repo.updateWord(word)
        reloadWords()
    }

    fun deleteWord(word: Word) = viewModelScope.launch {
        repo.deleteWord(word)
        reloadWords()
    }

    private fun getAllWordsInStudyCollection(collection: StudyCollection) = viewModelScope.launch {
        _allWords.value = repo.getWordsInCollection(collection)
    }

    private fun getCollectionById(id: Long) = viewModelScope.launch {
        collection.value = repo.getCollectionById(id)
    }

    fun openDialog() {
        openDialog = true
    }

    fun closeDialog() {
        openDialog = false
    }

    private suspend fun reloadWords() {
        Log.v("VIEWMODEL", "reload words")
        if(collection.value != null && collection.value!!.collectionId != null){
            _allWords.value = (repo.getWordsInCollection(collection.value!!))
        }
        Log.v("VIEWMODEL", "new data loaded")
    }
}