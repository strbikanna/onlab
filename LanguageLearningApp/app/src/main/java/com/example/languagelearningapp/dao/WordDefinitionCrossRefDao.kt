package com.example.languagelearningapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.languagelearningapp.model.WordDefinitionCrossRef
@Dao
interface WordDefinitionCrossRefDao {
    @Insert
    suspend fun add(crossRef : WordDefinitionCrossRef)

    @Delete
    suspend fun delete(crossRef : WordDefinitionCrossRef)
    @Query(
     "DELETE FROM WordDefinitionCrossRef " +
             "WHERE wordId=:id"
    )
    suspend fun deleteByWordId(id: Long)
}