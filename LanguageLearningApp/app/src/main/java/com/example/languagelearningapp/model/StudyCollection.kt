package com.example.languagelearningapp.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["collectionId"])])
data class StudyCollection(
    @PrimaryKey(autoGenerate = true) val collectionId: Long? = null,
    val name: String,
    val favorite: Boolean = false,
)