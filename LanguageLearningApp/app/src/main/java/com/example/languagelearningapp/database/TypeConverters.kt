package com.example.languagelearningapp.database

import androidx.room.TypeConverter
import com.example.languagelearningapp.translation.Language

class TypeConverters {
    @TypeConverter
    fun fromLanguageToString(value: Language?): String? {
        return value?.let {
            it.languageCode
        }
    }

    @TypeConverter
    fun fromStringToLanguage(value: String?): Language? {
        return value?.let {
            Language(it)
        }
    }
}