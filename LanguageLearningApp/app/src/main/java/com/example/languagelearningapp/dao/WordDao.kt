package com.example.languagelearningapp.dao

import androidx.room.*
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions

@Dao
interface WordDao {
    @Insert
    fun add(word: Word): Long

    @Insert
    fun addAll(words: List<Word>)

    @Update
    fun updateAll(words: List<Word>)
    @Update
    fun update(word: Word)

    @Delete
    fun delete(word: Word)

    @Query("SELECT * FROM Word ORDER BY expression ASC")
    fun getAllOrdered(): List<Word>

    @Query("SELECT * FROM Word WHERE wordId = :id")
    fun getById(id: Long): Word

    @Transaction
    @Query("SELECT * FROM Word WHERE wordId = :id")
    fun getOneWithDefinitionsById(id: Long): WordWithDefinitions

    @Transaction
    @Query("SELECT * FROM Word")
    fun getAllWithDefinitions(): List<WordWithDefinitions>

    @Transaction
    @Query("SELECT * FROM Word ORDER BY expression ASC")
    fun getAllWithDefinitionsOrdered(): List<WordWithDefinitions>

    @Query("SELECT * FROM Word WHERE learned = :learned")
    fun getLearned(learned: Boolean = true): List<Word>

    @Query("SELECT * FROM Word WHERE favorite = :favorite")
    fun getFavorites(favorite: Boolean = true): List<Word>


    @Transaction
    @Query(
        "SELECT * FROM Word " +
                "INNER JOIN WordDefinitionCrossRef ON WordDefinitionCrossRef.wordId = Word.wordId " +
                "INNER JOIN Definition ON WordDefinitionCrossRef.definitionId = Definition.definitionId " +
                "WHERE Definition.description = :definition"
    )
    @RewriteQueriesToDropUnusedColumns
    fun getAllByDefinition(definition: String): List<WordWithDefinitions>
}