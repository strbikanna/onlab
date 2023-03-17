package com.example.languagelearningapp.dao

import androidx.room.*
import com.example.languagelearningapp.model.StudyCollection
import com.example.languagelearningapp.model.StudyCollectionWithWords

@Dao
interface StudyCollectionDao {
    @Insert
    fun create(collection : StudyCollection): Long

    @Delete
    fun delete(collection : StudyCollection)

    @Update
    fun update(collection : StudyCollection)

    @Query("SELECT * FROM StudyCollection")
    fun getAll(): List<StudyCollection>

    @Transaction
    @Query("SELECT * FROM StudyCollection")
    fun getAllWithWords() : List<StudyCollectionWithWords>
}