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
        if (wordWithDefinitions.word.wordId != null || existsWord(wordWithDefinitions.word)) {
            val word = wordDao.getByExpression(wordWithDefinitions.word.expression)
            wordWithDefinitions.word = word!!
            updateWord(wordWithDefinitions)
            return
        }
        val wordId = wordDao.add(wordWithDefinitions.word)
        wordWithDefinitions.definitions.forEach { def ->
            if (existsDefinition(def)) {
                addExistingDefinitionToWord(def, wordWithDefinitions.word)
            } else {
                addDefinition(def, wordId)
            }

        }
    }

    private suspend fun addExistingDefinitionToWord(def: Definition, word: Word) {
        val existingDef = defDao.getByDescription(def.description) ?: return
        wordDefRefDao.add(WordDefinitionCrossRef(word.wordId!!, existingDef.definitionId!!))
    }

    private suspend fun existsDefinition(def: Definition): Boolean {
        return defDao.getByDescription(def.description) != null
    }

    private suspend fun existsWord(word: Word): Boolean {
        return wordDao.getByExpression(word.expression) != null
    }

    private suspend fun addDefinition(definition: Definition, wordId: Long) {
        if (definition.definitionId != null || existsDefinition(definition)) {
            val existingDef = defDao.getByDescription(definition.description)
            defDao.update(existingDef!!)
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

    suspend fun getFavoriteWords(): List<WordWithDefinitions> {
        return wordDao.getFavorites(true)
    }

    suspend fun getLearnedWords(): List<WordWithDefinitions> {
        return wordDao.getLearned(true)
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
        val wordId: Long
        if (word.word.wordId != null) {
            wordId = word.word.wordId!!
            updateWord(word)
            return
        } else {
            wordId = wordDao.add(word.word)
            word.definitions.forEach {
                addDefinition(it, wordId)
            }
        }
        val collId = if (collection.collectionId == null) {
            addCollection(collection)
        } else {
            collDao.getById(collection.collectionId).collectionId!!
        }
        wordCollRefDao.save(WordCollectionCrossRef(wordId, collId))
    }
}