package com.example.languagelearningapp.model

import androidx.room.Entity

@Entity(primaryKeys = ["wordId", "collectionId"])
data class WordCollectionCrossRef(
    val wordId: Long,
    val collectionId : Long
)