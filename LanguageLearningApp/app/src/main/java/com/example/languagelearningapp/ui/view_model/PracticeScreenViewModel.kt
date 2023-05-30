package com.example.languagelearningapp.ui.view_model

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagelearningapp.model.StudyCollection
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions
import com.example.languagelearningapp.repository.WordRepository
import com.example.languagelearningapp.ui.screens.practice_screen.components.TransitionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeScreenViewModel @Inject constructor(
    private val repo: WordRepository
) : ViewModel() {
    private var collection = MutableLiveData(StudyCollection(name = ""))

    private var _allWords: MediatorLiveData<List<WordWithDefinitions>> =
        MediatorLiveData(emptyList())
    var allWords: MediatorLiveData<List<WordWithDefinitions>> = _allWords
    var revealedState = MutableLiveData(TransitionState.Zero)
    var swipeState = MutableLiveData(true)

    init {
        _allWords.addSource(
            collection
        ) {
            if (it.collectionId != null)
                getAllWordsInStudyCollection(it)
        }
    }

    fun resetRevealedState() {
        revealedState.value = TransitionState.Zero
    }

    fun resetSwipeState() {
        swipeState.value = true
    }

    fun updateWord(word: Word) = viewModelScope.launch {
        repo.updateWord(word)
    }

    fun initForCollection(id: Long) = viewModelScope.launch {
        collection.value = repo.getCollectionById(id)
    }

    private fun getAllWordsInStudyCollection(collection: StudyCollection) = viewModelScope.launch {
        _allWords.value = repo.getWordsInCollection(collection)
    }


}