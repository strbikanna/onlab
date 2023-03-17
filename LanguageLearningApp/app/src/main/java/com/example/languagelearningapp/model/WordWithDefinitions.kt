package com.example.languagelearningapp.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class WordWithDefinitions(
    @Embedded val word: Word,
    @Relation(
        parentColumn = "wordId",
        entityColumn = "definitionId",
        associateBy = Junction(WordDefinitionCrossRef::class)
    )
    val definitions: List<Definition>
)