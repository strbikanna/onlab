package com.example.languagelearningapp.translation

import java.util.*

data class Language(
    val languageCode: String,
    val displayName: String = Locale(languageCode).displayName
)
