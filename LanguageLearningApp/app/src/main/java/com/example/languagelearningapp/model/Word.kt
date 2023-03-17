package com.example.languagelearningapp.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(indices = [Index(value = ["wordId"])])
data class Word(
    @PrimaryKey(autoGenerate = true) val wordId: Long? = null,
    val expression: String,
    val wordClass: WordClass? = null,
    val favorite: Boolean = false,
    val learned: Boolean = false,
){
    enum class WordClass {
        NOUN, VERB, ADJECTIVE;
        companion object {
            @JvmStatic
            @TypeConverter
            fun getByText(text: String): WordClass {
                return valueOf(text)
            }

            @JvmStatic
            @TypeConverter
            fun toText(wClass: WordClass): String {
                return wClass.name
            }
        }
    }
}
