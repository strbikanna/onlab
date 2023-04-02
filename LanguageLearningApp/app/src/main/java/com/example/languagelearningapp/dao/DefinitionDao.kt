package com.example.languagelearningapp.dao

import androidx.room.*
import com.example.languagelearningapp.model.Definition
@Dao
interface DefinitionDao {
    @Insert
    suspend fun add(def: Definition): Long

    @Delete
    fun delete(def: Definition)

    @Update
    fun update(def: Definition)

    @Query("SELECT * FROM Definition WHERE description = :description")
    fun getByDescription(description : String): Definition

    @Query("SELECT * FROM Definition WHERE definitionId = :id")
    fun getById(id: Long): Definition
}