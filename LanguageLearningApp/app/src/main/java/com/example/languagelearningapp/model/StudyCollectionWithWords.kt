package com.example.languagelearningapp.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class StudyCollectionWithWords(
    @Embedded val collection: StudyCollection,
    @Relation(
        parentColumn = "collectionId",
        entityColumn = "wordId",
        associateBy = Junction(WordCollectionCrossRef::class)
    )
    val words: List<Word>
)