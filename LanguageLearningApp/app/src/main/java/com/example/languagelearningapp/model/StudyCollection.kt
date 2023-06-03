package com.example.languagelearningapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.languagelearningapp.translation.Language
import com.google.mlkit.nl.translate.TranslateLanguage

@Entity(indices = [Index(value = ["collectionId"])])
data class StudyCollection(
    @PrimaryKey(autoGenerate = true) val collectionId: Long? = null,
    val name: String,
    val favorite: Boolean = false,
    @ColumnInfo(defaultValue = "en")
    val sourceLanguage: Language = Language(TranslateLanguage.ENGLISH),
    @ColumnInfo(defaultValue = "en")
    val targetLanguage: Language = Language(TranslateLanguage.ENGLISH)
)