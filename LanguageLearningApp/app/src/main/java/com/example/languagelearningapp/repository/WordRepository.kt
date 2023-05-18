package com.example.languagelearningapp.repository

import com.example.languagelearningapp.dao.*
import com.example.languagelearningapp.error.WrongDataException
import com.example.languagelearningapp.model.*
import javax.inject.Inject

class WordRepository @Inject constructor(
    private val wordDao: WordDao,
    private val defDao: DefinitionDao,
    private val wordDefRefDao: WordDefinitionCrossRefDao,
    private val collDao: StudyCollectionDao,
    private val wordCollRefDao: WordCollectionCrossRefDao
) {
    suspend fun getAllWords() = wordDao.getAllWithDefinitions()

    suspend fun getWordById(id: Long) = wordDao.getById(id)

    suspend fun getWordsByDefinition(definition: Definition) =
        wordDao.getAllByDefinition(definition.description)

    suspend fun addWord(wordWithDefinitions: WordWithDefinitions) {
        if (wordWithDefinitions.word.wordId != null) {
            updateWord(wordWithDefinitions)
            return
        }
        val wordId = wordDao.add(wordWithDefinitions.word)
        wordWithDefinitions.definitions.forEach {
            addDefinition(it, wordId)
        }
    }

    private suspend fun addDefinition(definition: Definition, wordId: Long) {
        if (definition.definitionId != null) {
            defDao.update(definition)
            return
        }
        val defId = defDao.add(definition)
        wordDefRefDao.add(WordDefinitionCrossRef(wordId, defId))
    }

    suspend fun updateWord(word: Word) {
        wordDao.update(word)
    }

    suspend fun updateWord(wordWithDefinition: WordWithDefinitions) {
        val word = wordWithDefinition.word
        if (word.wordId == null) {
            addWord(wordWithDefinition)
            return
        }
        wordDao.update(word)

        wordWithDefinition.definitions.forEach {
            if (it.definitionId == null) {
                addDefinition(it, word.wordId)
            } else {
                defDao.update(it)
            }
        }
        val oldDefinitions = wordDao.getOneWithDefinitionsById(word.wordId).definitions
        oldDefinitions.forEach { def ->
            if (wordWithDefinition.definitions.find { it.definitionId == def.definitionId } == null) {
                wordDefRefDao.deleteByDefinitionId(def.definitionId!!)
            }
        }
    }

    suspend fun deleteWord(word: Word) {
        wordDao.delete(word)
        wordCollRefDao.deleteByWordId(word.wordId!!)
    }

    suspend fun getWordsInCollection(collection: StudyCollection): List<WordWithDefinitions> {
        if (collection.collectionId == null)
            throw WrongDataException("Collection must have id.")
        return wordDao.getAllByCollection(collection.collectionId)
    }

    suspend fun getAllCollections() = collDao.getAll()
    suspend fun addCollection(collection: StudyCollection) = collDao.add(collection)
    suspend fun deleteCollection(collection: StudyCollection) {
        collDao.delete(collection)
        wordCollRefDao.deleteByCollectionId(collection.collectionId!!)
    }

    suspend fun getCollectionById(id: Long) = collDao.getById(id)
    suspend fun addWordToCollection(word: WordWithDefinitions, collection: StudyCollection) {
        if (word.word.wordId != null) {
            updateWord(word)
            return
        }
        val wordId = wordDao.add(word.word)
        word.definitions.forEach {
            addDefinition(it, wordId)
        }
        val collId = collDao.getById(collection.collectionId!!).collectionId!!
        wordCollRefDao.add(WordCollectionCrossRef(wordId, collId))
    }
}