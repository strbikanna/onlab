package com.example.languagelearningapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.languagelearningapp.model.WordDefinitionCrossRef
@Dao
interface WordDefinitionCrossRefDao {
    @Insert
    fun add(crossRef : WordDefinitionCrossRef)

    @Delete
    fun delete(crossRef : WordDefinitionCrossRef)
}