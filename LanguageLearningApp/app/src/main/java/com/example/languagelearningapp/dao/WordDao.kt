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

    @Query("SELECT * FROM Word WHERE expression = :exp")
    suspend fun getByExpression(exp: String): Word?

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
    suspend fun getLearned(learned: Boolean = true): List<WordWithDefinitions>

    @Query("SELECT * FROM Word WHERE favorite = :favorite")
    suspend fun getFavorites(favorite: Boolean = true): List<WordWithDefinitions>


    @Transaction
    @Query(
        "SELECT * FROM Word " +
                "INNER JOIN WordDefinitionCrossRef ON WordDefinitionCrossRef.wordId = Word.wordId " +
                "INNER JOIN Definition ON WordDefinitionCrossRef.definitionId = Definition.definitionId " +
                "WHERE Definition.description = :definition"
    )
    @RewriteQueriesToDropUnusedColumns
    suspend fun getAllByDefinition(definition: String): List<WordWithDefinitions>

    @Transaction
    @Query(
        "SELECT * FROM Word " +
                "INNER JOIN WordCollectionCrossRef ON WordCollectionCrossRef.wordId = Word.wordId " +
                "INNER JOIN StudyCollection ON WordCollectionCrossRef.collectionId = StudyCollection.collectionId " +
                "WHERE StudyCollection.collectionID = :collectionId"
    )
    @RewriteQueriesToDropUnusedColumns
    suspend fun getAllByCollection(collectionId: Long): List<WordWithDefinitions>

    @Transaction
    @Query(
        "DELETE FROM Word " +
                "WHERE wordId IN(" +
                "SELECT w.wordId FROM Word w " +
                "INNER JOIN WordCollectionCrossRef ON WordCollectionCrossRef.wordId = w.wordId " +
                "INNER JOIN StudyCollection ON WordCollectionCrossRef.collectionId = StudyCollection.collectionId " +
                "WHERE StudyCollection.collectionID = :collectionId)"
    )
    suspend fun deleteAllInCollection(collectionId: Long)
}