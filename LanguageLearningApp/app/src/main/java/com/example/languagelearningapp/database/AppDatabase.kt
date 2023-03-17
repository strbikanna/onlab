package com.example.languagelearningapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.languagelearningapp.dao.StudyCollectionDao
import com.example.languagelearningapp.dao.WordDao
import com.example.languagelearningapp.model.*

@Database(entities = [Word::class,
    Definition::class,
    StudyCollection::class,
    WordCollectionCrossRef::class,
    WordDefinitionCrossRef::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun studyCollectionDao(): StudyCollectionDao

    companion object {
        fun getDatabase(applicationContext: Context): AppDatabase {
            return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "language-learning-db"
            ).build();
        }
    }
}