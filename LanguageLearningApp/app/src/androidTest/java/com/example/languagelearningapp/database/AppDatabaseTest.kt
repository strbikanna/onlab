package com.example.languagelearningapp.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.languagelearningapp.dao.*
import com.example.languagelearningapp.model.*
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest : TestCase(){

    private lateinit var db: AppDatabase
    private lateinit var wordDao: WordDao
    private lateinit var collectionDao: StudyCollectionDao
    private lateinit var wordCollectionDao: WordCollectionCrossRefDao
    private lateinit var wordDefinitionDao: WordDefinitionCrossRefDao
    private lateinit var definitionDao: DefinitionDao
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        wordDao = db.wordDao()
        collectionDao = db.studyCollectionDao()
        wordCollectionDao = db.wordCollectionDao()
        wordDefinitionDao = db.wordDefinitionDao()
        definitionDao = db.definitionDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun saveAndGetWord() {
        val testWord = Word(
            expression = "new test word",
            wordClass = Word.WordClass.NOUN,
            favorite = true
        )
        CoroutineScope(Dispatchers.Default + Job()).launch {
            val id = wordDao.add(testWord)
            val wordList = wordDao.getAllOrdered()
            assertTrue(wordList.isNotEmpty())
            assertEquals(id, wordList[0].wordId)
            assertEquals(wordList[0], wordDao.getById(id))
        }

    }

    @Test
    fun addWordToCollection(){
        val coll = StudyCollection(
            name = "testCollection",
            favorite = false,
        )
        val testWord = Word(
            expression = "another test word",
            wordClass = Word.WordClass.NOUN,
            favorite = true
        )
        CoroutineScope(Dispatchers.Default + Job()).launch {
            val wordId = wordDao.add(testWord)
            val collId = collectionDao.add(coll)
            wordCollectionDao.add(WordCollectionCrossRef(wordId, collId))
            val collWithWords = collectionDao.getAllWithWords()
            assertTrue(collWithWords.isNotEmpty())
            val savedWord = collWithWords[0].words[0]
            assertEquals(testWord.expression, savedWord.expression)
            assertEquals(wordId, savedWord.wordId)
            assertEquals(collId, collWithWords[0].collection.collectionId)
        }
    }
    @Test
    fun addAndGetWordWithDefinitions(){
        val testWord = Word(
            expression = "test word with meanings",
            wordClass = Word.WordClass.NOUN,
            favorite = true
        )
        val def1 = Definition(description = "meaning of test word")
        val def2 = Definition(description = "translation of test word")
        val def3 = Definition(description = "associated with test word")
        CoroutineScope(Dispatchers.Default + Job()).launch {
            val wordId = wordDao.add(testWord)
            val def1id = definitionDao.add(def1)
            val def2id = definitionDao.add(def2)
            val def3id = definitionDao.add(def3)

            wordDefinitionDao.add(WordDefinitionCrossRef(wordId, def1id))
            wordDefinitionDao.add(WordDefinitionCrossRef(wordId, def2id))
            wordDefinitionDao.add(WordDefinitionCrossRef(wordId, def3id))

            val wordDefList = wordDao.getAllWithDefinitions()
            assertTrue(wordDefList.isNotEmpty())
            assertEquals(3, wordDefList[0].definitions.size)
            val def1Saved = wordDefList[0].definitions[0]
            assertEquals(def1Saved.description, def1.description)
        }
    }

    @Test
    fun getWordsByDefinition(){
        val testWord1 = Word(
            expression = "test word",
        )
        val testWord2 = Word(
            expression = "test word with same meaning",
        )
        val def = Definition(description = "meaning of test word")
        CoroutineScope(Dispatchers.Default + Job()).launch {
            val wordId1 = wordDao.add(testWord1)
            val wordId2 = wordDao.add(testWord2)
            val defId = definitionDao.add(def)

            wordDefinitionDao.add(WordDefinitionCrossRef(wordId1, defId))
            wordDefinitionDao.add(WordDefinitionCrossRef(wordId2, defId))

            val wordList = wordDao.getAllByDefinition(def.description)
            assertTrue(wordList.isNotEmpty())
            assertEquals(2, wordList.size)
            var savedWord1 = wordList[0]
            val savedWord2 = wordList[1]
            assertEquals(def.description, savedWord1.definitions[0].description)
            assertEquals(
                savedWord1.definitions[0].description,
                savedWord2.definitions[0].description
            )
            savedWord1 = wordDao.getOneWithDefinitionsById(wordId1)
            assertEquals(def.description, savedWord1.definitions[0].description)
        }
    }

}