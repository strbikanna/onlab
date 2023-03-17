package com.example.languagelearningapp.model

import androidx.room.Entity

@Entity(primaryKeys = ["wordId", "definitionId"])
data class WordDefinitionCrossRef(
    val wordId : Long,
    val definitionId: Long,
)