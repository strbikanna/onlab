package com.example.languagelearningapp.dao

import androidx.room.*
import com.example.languagelearningapp.model.StudyCollection
import com.example.languagelearningapp.model.StudyCollectionWithWords

@Dao
interface StudyCollectionDao {
    @Insert
    suspend fun add(collection : StudyCollection): Long

    @Delete
    suspend fun delete(collection : StudyCollection)

    @Update
    suspend fun update(collection : StudyCollection)

    @Query("SELECT * FROM StudyCollection")
    suspend fun getAll(): List<StudyCollection>
    @Query("SELECT * FROM StudyCollection WHERE collectionId = :id")
    suspend fun getById(id: Long): StudyCollection

    @Transaction
    @Query("SELECT * FROM StudyCollection")
    suspend fun getAllWithWords() : List<StudyCollectionWithWords>
}