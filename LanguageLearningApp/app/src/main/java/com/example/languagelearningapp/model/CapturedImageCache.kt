package com.example.languagelearningapp.model

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage

class CapturedImageCache {
    companion object {
        val capturedImageHolder: MutableMap<String, InputImage> = mutableMapOf()

        @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
        fun addImageProxy(key: String, image: ImageProxy) {
            val inputImage =
                InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees)
            capturedImageHolder[key] = inputImage
        }

        fun addUri(context: Context, uri: Uri): String {
            val key = "uri"
            val inputImage = InputImage.fromFilePath(context, uri)
            capturedImageHolder[key] = inputImage
            return key
        }
    }
}