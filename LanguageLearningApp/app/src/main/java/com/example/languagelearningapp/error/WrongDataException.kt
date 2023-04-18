package com.example.languagelearningapp.error

class WrongDataException(override val message: String): RuntimeException(message) {
}