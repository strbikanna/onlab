package com.example.languagelearningapp.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.languagelearningapp.dao.*
import com.example.languagelearningapp.model.*

@Database(
    entities = [Word::class,
        Definition::class,
        StudyCollection::class,
        WordCollectionCrossRef::class,
        WordDefinitionCrossRef::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    exportSchema = true
)
@androidx.room.TypeConverters(TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun definitionDao(): DefinitionDao
    abstract fun studyCollectionDao(): StudyCollectionDao
    abstract fun wordCollectionDao(): WordCollectionCrossRefDao
    abstract fun wordDefinitionDao(): WordDefinitionCrossRefDao

    companion object {
        fun getDatabase(applicationContext: Context): AppDatabase {
            return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "language-learning-db"
            ).build()
        }
    }
}