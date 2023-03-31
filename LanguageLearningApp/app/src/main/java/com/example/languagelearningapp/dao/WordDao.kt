package com.example.languagelearningapp.dao

import androidx.room.*
import com.example.languagelearningapp.model.Word
import com.example.languagelearningapp.model.WordWithDefinitions

@Dao
interface WordDao {
    @Insert
    suspend fun add(word: Word): Long

    @Insert
    suspend fun addAll(words: List<Word>)

    @Update
    suspend fun updateAll(words: List<Word>)
    @Update
    suspend fun update(word: Word)

    @Delete
    suspend fun delete(word: Word)

    @Query("SELECT * FROM Word ORDER BY expression ASC")
    suspend fun getAllOrdered(): List<Word>

    @Query("SELECT * FROM Word WHERE wordId = :id")
    suspend fun getById(id: Long): Word

    @Transaction
    @Query("SELECT * FROM Word WHERE wordId = :id")
    suspend fun getOneWithDefinitionsById(id: Long): WordWithDefinitions

    @Transaction
    @Query("SELECT * FROM Word")
    suspend fun getAllWithDefinitions(): List<WordWithDefinitions>

    @Transaction
    @Query("SELECT * FROM Word ORDER BY expression ASC")
    suspend fun getAllWithDefinitionsOrdered(): List<WordWithDefinitions>

    @Query("SELECT * FROM Word WHERE learned = :learned")
    suspend fun getLearned(learned: Boolean = true): List<Word>

    @Query("SELECT * FROM Word WHERE favorite = :favorite")
    suspend fun getFavorites(favorite: Boolean = true): List<Word>


    @Transaction
    @Query(
        "SELECT * FROM Word " +
                "INNER JOIN WordDefinitionCrossRef ON WordDefinitionCrossRef.wordId = Word.wordId " +
                "INNER JOIN Definition ON WordDefinitionCrossRef.definitionId = Definition.definitionId " +
                "WHERE Definition.description = :definition"
    )
    @RewriteQueriesToDropUnusedColumns
    suspend fun getAllByDefinition(definition: String): List<WordWithDefinitions>
}