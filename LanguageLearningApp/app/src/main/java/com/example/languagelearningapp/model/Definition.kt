package com.example.languagelearningapp.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["definitionId"])])
data class Definition (
    @PrimaryKey(autoGenerate = true) val definitionId: Long? = null,
    val description: String,
)
