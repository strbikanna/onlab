package com.example.languagelearningapp.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["description"], unique = true)])
data class Definition(
    @PrimaryKey(autoGenerate = true) val definitionId: Long? = null,
    val description: String,
)
