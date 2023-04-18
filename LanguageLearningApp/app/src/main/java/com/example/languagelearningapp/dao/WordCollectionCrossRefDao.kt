package com.example.languagelearningapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.languagelearningapp.model.WordCollectionCrossRef
@Dao
interface WordCollectionCrossRefDao {
    @Insert
    suspend fun add(crossRef : WordCollectionCrossRef)

    @Delete
    suspend fun delete(crossRef : WordCollectionCrossRef)
    @Query(
        "DELETE FROM WordCollectionCrossRef " +
                "WHERE wordId=:id"
    )
    suspend fun deleteByWordId(id: Long)
}