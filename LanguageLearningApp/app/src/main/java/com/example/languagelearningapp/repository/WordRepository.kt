package com.example.languagelearningapp.repository

import androidx.lifecycle.viewModelScope
import com.example.languagelearningapp.dao.DefinitionDao
import com.example.languagelearningapp.dao.WordDao
import com.example.languagelearningapp.dao.WordDefinitionCrossRefDao
import com.example.languagelearningapp.model.Definition
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordDefinitionCrossRef
import com.example.languagelearningapp.model.WordWithDefinitions
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordRepository @Inject constructor(
    private val wordDao: WordDao,
    private val defDao: DefinitionDao,
    private val wordDefRefDao: WordDefinitionCrossRefDao
)  {
    fun getAllWords() = wordDao.getAllWithDefinitions()

    fun getWordById(id: Long) =  wordDao.getById(id)

    fun getWordsByDefinition(definition: Definition) = wordDao.getAllByDefinition(definition.description)

    fun addWord(wordWithDefinitions: WordWithDefinitions){
        val wordId = wordDao.add(wordWithDefinitions.word)
        wordWithDefinitions.definitions.forEach {
            addDefinition(it, wordId)
        }
    }

    private fun addDefinition( definition: Definition, wordId: Long) {
        val defId = defDao.add(definition)
        wordDefRefDao.add(WordDefinitionCrossRef(wordId, defId))
    }

    fun updateWord(word: Word) {
        wordDao.update(word)
    }
    fun updateWord(wordWithDefinition: WordWithDefinitions) {
        val word = wordWithDefinition.word
        if(word.wordId == null){
            addWord(wordWithDefinition)
            return
        }
        wordDao.update(word)
        wordWithDefinition.definitions.forEach{
            if(it.definitionId == null){
                addDefinition(it, word.wordId)
            }
            else{
                defDao.update(it)
            }
        }
    }

    fun deleteWord(word: Word)  {
        wordDao.delete(word)
    }
}