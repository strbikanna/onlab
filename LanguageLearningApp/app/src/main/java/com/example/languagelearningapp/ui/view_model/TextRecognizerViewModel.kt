package com.example.languagelearningapp.ui.view_model

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.internal.ImageConvertUtils
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.Element
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class TextRecognizerViewModel : ViewModel() {
    var image: InputImage? = null
        set(value) {
            Log.d(MANUAL_TESTING_LOG, "Input image added. Processing started.")
            field = value
            processImage()
        }
    val resultText: MutableLiveData<TextOrError> = MutableLiveData()
    var chosenElement: MutableLiveData<Element?> = MutableLiveData()
    private var recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun getImageToShow(inputImage: InputImage): ImageBitmap {
        val bitmap = ImageConvertUtils.getInstance().getUpRightBitmap(inputImage).asImageBitmap()
        Log.d("TEXTVIEW", "Bitmap size: ${bitmap.width}, ${bitmap.height}")
        return bitmap
    }

    fun getWordByPosition(position: Offset) {
        var word: Element? = null
        for (block in resultText.value!!.text!!.textBlocks) {
            for (line in block.lines) {
                val found = line.elements.find { element ->
                    if (element.boundingBox != null) {
                        element.boundingBox!!.contains(position.x.toInt(), position.y.toInt())
                    } else {
                        false
                    }
                }
                if (found != null) {
                    word = found
                    break
                }
            }
            if (word != null)
                break
        }
        chosenElement.value = word
        Log.d("TextModel", "Word found: ${word?.text}")
    }

    private fun processImage() {
        if (image == null) return
        recognizer.process(image!!)
            .addOnSuccessListener {
                Log.d(MANUAL_TESTING_LOG, "Processing finished with success.")
                resultText.value = TextOrError(it, null)
                //logExtrasForTesting(it)
            }
            .addOnFailureListener {
                Log.d(MANUAL_TESTING_LOG, "Processing finished with failure.")
                resultText.value = TextOrError(null, it)
            }
    }

    data class TextOrError(val text: Text?, val error: Exception?)

    companion object {
        private const val MANUAL_TESTING_LOG = "TextRecProcessor"
        private fun logExtrasForTesting(text: Text?) {
            if (text != null) {
                Log.v(MANUAL_TESTING_LOG, "Detected text has : " + text.textBlocks.size + " blocks")
                for (i in text.textBlocks.indices) {
                    val lines = text.textBlocks[i].lines
                    Log.v(
                        MANUAL_TESTING_LOG,
                        String.format("Detected text block %d has %d lines", i, lines.size)
                    )
                    for (j in lines.indices) {
                        val elements = lines[j].elements
                        Log.v(
                            MANUAL_TESTING_LOG,
                            String.format("Detected text line %d has %d elements", j, elements.size)
                        )
                        for (k in elements.indices) {
                            val element = elements[k]
                            Log.v(
                                MANUAL_TESTING_LOG,
                                String.format("Detected text element %d says: %s", k, element.text)
                            )
                            Log.v(
                                MANUAL_TESTING_LOG,
                                String.format(
                                    "Detected text element %d has a bounding box: %s",
                                    k,
                                    element.boundingBox!!.flattenToString()
                                )
                            )
                            Log.v(
                                MANUAL_TESTING_LOG,
                                String.format(
                                    "Expected corner point size is 4, get %d",
                                    element.cornerPoints!!.size
                                )
                            )
                            for (point in element.cornerPoints!!) {
                                Log.v(
                                    MANUAL_TESTING_LOG,
                                    String.format(
                                        "Corner point for element %d is located at: x - %d, y = %d",
                                        k,
                                        point.x,
                                        point.y
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}