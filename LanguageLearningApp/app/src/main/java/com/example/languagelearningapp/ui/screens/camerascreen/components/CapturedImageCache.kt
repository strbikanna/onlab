package com.example.languagelearningapp.ui.screens.camerascreen.components

import androidx.camera.core.ImageProxy

class CapturedImageCache {
    companion object {
        val globalImageHolder: MutableMap<String, ImageProxy> = mutableMapOf()
    }
}