package com.example.languagelearningapp.dao

import androidx.room.*
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions

@Dao
interface WordDao {
    @Insert
    fun insert(word: Word): Long

    @Insert
    fun insertAll(words: List<Word>)

    @Update
    fun updateAll(words: List<Word>)

    @Delete
    fun delete(word: Word)

    @Query("SELECT * FROM Word ORDER BY expression ASC")
    fun getAllOrdered (): List<Word>

    @Transaction
    @Query("SELECT * FROM Word")
    fun getAllWithDefinitions(): List<WordWithDefinitions>

    @Transaction
    @Query("SELECT * FROM Word ORDER BY expression ASC")
    fun getAllWithDefinitionsOrdered(): List<WordWithDefinitions>

    @Query("SELECT * FROM Word WHERE learned = :learned")
    fun getLearned(learned :Boolean = true) : List<Word>

    @Query("SELECT * FROM Word WHERE favorite = :favorite")
    fun getFavorites(favorite :Boolean = true) : List<Word>

    //@Query("SELECT * FROM Word WHERE description = :definition")
    //fun getAllByDefinition(definition : String) : List<WordWithDefinitions>
}