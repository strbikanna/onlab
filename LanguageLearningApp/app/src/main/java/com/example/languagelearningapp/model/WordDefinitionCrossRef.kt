package com.example.languagelearningapp.model

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["wordId", "definitionId"], indices = [Index(value = ["definitionId"])])
data class WordDefinitionCrossRef(
    val wordId: Long,
    val definitionId: Long,
)