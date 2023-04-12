package com.example.languagelearningapp.translation

import com.google.mlkit.nl.translate.TranslateLanguage
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class TranslatorTest {
    val translator = TranslatorViewModel()
    val english = Language(TranslateLanguage.ENGLISH)
    val hungarian = Language(TranslateLanguage.HUNGARIAN)

    @Before
    fun setUp() {
        translator.sourceLanguage=english
        translator.targetLanguage=hungarian
    }

    @Test
    fun downloadLanguagesTest() {
        assertFalse(translator.requiresModelDownload(lang = english))
        assertFalse(translator.requiresModelDownload(lang = hungarian))
    }

    @Test
    fun translateTest() {



    }
}