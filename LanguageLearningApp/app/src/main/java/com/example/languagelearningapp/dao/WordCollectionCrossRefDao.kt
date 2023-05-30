package com.example.languagelearningapp.dao

import androidx.room.*
import com.example.languagelearningapp.model.WordCollectionCrossRef

@Dao
interface WordCollectionCrossRefDao {
    @Upsert
    suspend fun save(crossRef: WordCollectionCrossRef)

    @Delete
    suspend fun delete(crossRef: WordCollectionCrossRef)

    @Query(
        "DELETE FROM WordCollectionCrossRef " +
                "WHERE wordId=:id"
    )
    suspend fun deleteByWordId(id: Long)

    @Query(
        "DELETE FROM WordCollectionCrossRef " +
                "WHERE collectionId=:id"
    )
    suspend fun deleteByCollectionId(id: Long)

    @Query(
        "UPDATE WordCollectionCrossRef " +
                "SET collectionId = :newCollectionId " +
                "WHERE collectionId = :oldCollectionId"
    )
    suspend fun moveWordsToCollection(oldCollectionId: Long, newCollectionId: Long)

}