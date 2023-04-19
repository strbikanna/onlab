package com.example.languagelearningapp.text_recognition

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.languagelearningapp.R
import com.google.mlkit.vision.common.InputImage


@Composable
fun TextRecognizerScreen(
    viewModel: TextRecognizerViewModel = TextRecognizerViewModel()
) {
    val ctx = LocalContext.current
    val testImage = getBitmapFromImage(ctx, R.drawable.android)
    Image(
        bitmap = testImage.asImageBitmap(),
        contentDescription = "test image"
    )
    Log.d("TextRecogView", "Adding inputimage...")
    viewModel.image = InputImage.fromBitmap(testImage, 0)

    /**
     * TODO
     * add inputimage to viewmodel
     * collect textorerror as state
     * display recognized block cornerpoints
     * display language tag
     */

}

private fun getBitmapFromImage(context: Context, drawable: Int): Bitmap {

    val db = ContextCompat.getDrawable(context, drawable)
    val bit = Bitmap.createBitmap(
        db!!.intrinsicWidth, db.intrinsicHeight, Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bit)

    db.setBounds(0, 0, canvas.width, canvas.height)


    db.draw(canvas)

    return bit
}