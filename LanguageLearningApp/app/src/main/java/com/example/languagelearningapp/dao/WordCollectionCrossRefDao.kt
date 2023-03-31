package com.example.languagelearningapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.example.languagelearningapp.model.WordCollectionCrossRef
@Dao
interface WordCollectionCrossRefDao {
    @Insert
    suspend fun add(crossRef : WordCollectionCrossRef)

    @Delete
    suspend fun delete(crossRef : WordCollectionCrossRef)
}