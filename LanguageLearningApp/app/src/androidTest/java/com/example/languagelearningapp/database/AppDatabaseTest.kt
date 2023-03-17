package com.example.languagelearningapp.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.languagelearningapp.dao.StudyCollectionDao
import com.example.languagelearningapp.dao.WordDao
import com.example.languagelearningapp.model.Word
import junit.framework.TestCase
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
    private lateinit var testWord : Word

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        wordDao = db.wordDao()
        collectionDao = db.studyCollectionDao()
        testWord = Word(
            expression = "new test word",
            wordClass = Word.WordClass.NOUN,
            favorite = true
        )
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun saveAndGetWord() {
        val id = wordDao.insert(testWord)
        val wordList = wordDao.getAllOrdered()
        assertTrue(wordList.isNotEmpty())
        assertEquals(id, wordList[0].wordId)
    }

}