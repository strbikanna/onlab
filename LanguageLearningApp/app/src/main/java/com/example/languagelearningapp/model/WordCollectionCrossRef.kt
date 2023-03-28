package com.example.languagelearningapp.model

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["wordId", "collectionId"], indices = [Index(value=["collectionId"])])
data class WordCollectionCrossRef(
    val wordId: Long,
    val collectionId : Long
)