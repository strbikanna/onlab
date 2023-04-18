package com.example.languagelearningapp.dao

import androidx.room.*
import com.example.languagelearningapp.model.Definition
@Dao
interface DefinitionDao {
    @Insert
    suspend fun add(def: Definition): Long

    @Delete
    suspend fun delete(def: Definition)

    @Update
    suspend fun update(def: Definition)

    @Query("SELECT * FROM Definition WHERE description = :description")
    suspend fun getByDescription(description : String): Definition

    @Query("SELECT * FROM Definition WHERE definitionId = :id")
    suspend fun getById(id: Long): Definition
}