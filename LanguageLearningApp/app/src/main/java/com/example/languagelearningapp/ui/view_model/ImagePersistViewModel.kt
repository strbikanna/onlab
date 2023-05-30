package com.example.languagelearningapp.ui.view_model

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImagePersistViewModel : ViewModel() {
    private val format = Bitmap.CompressFormat.JPEG
    fun loadImage(context: Context, uri: Uri): ImageBitmap {
        val bitmap: ImageBitmap
        context.contentResolver
            .openInputStream(uri).use { stream ->
                bitmap = BitmapFactory.decodeStream(stream).asImageBitmap()
            }
        return bitmap
    }

    fun deleteImage(context: Context, uri: Uri) {
        context.contentResolver.delete(uri, null, null)
    }

    fun canUseThumbnail(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @RequiresApi(Build.VERSION_CODES.Q)
    fun loadThumbnail(context: Context, uri: Uri): Bitmap {
        return context.contentResolver.loadThumbnail(
            uri, Size(400, 400), null
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun loadAllThumbnails(context: Context): Map<Uri, Bitmap> {
        val thumbnails: MutableMap<Uri, Bitmap> = mutableMapOf()
        val uriList = loadImageUris(context)
        for (uri in uriList) {
            val thumbnail = context.contentResolver.loadThumbnail(
                uri, Size(350, 350), null
            )
            thumbnails[uri] = thumbnail
        }
        return thumbnails
    }

    private val mediaType = "image/jpeg"

    private val path = "Pictures/Text-Images"

    fun saveImage(context: Context, bitmap: Bitmap, fileName: String?): Uri {
        val name = fileName ?: SimpleDateFormat("mm-dd-yyyy", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, mediaType)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, path)
            }
        }
        val collectionUri = getCollectionUri()
        var recordUri: Uri? = null
        val savedUri = kotlin.runCatching {
            with(context.contentResolver) {
                insert(collectionUri, contentValues)?.also { uri ->
                    recordUri = uri
                    openOutputStream(uri)?.use { stream ->
                        if (!bitmap.compress(format, 100, stream)) {
                            throw IOException("Failed to save image.")
                        }
                    } ?: throw IOException("Failed to access storage.")
                } ?: throw IOException("Failed to create MediaStore item.")
            }
        }.getOrElse { error ->
            recordUri?.let {
                context.contentResolver.delete(it, null, null)
            }
            throw error
        }
        return savedUri
    }

    private fun loadImageUris(context: Context): List<Uri> {
        val uriList: MutableList<Uri> = mutableListOf()
        val projections = arrayOf(
            MediaStore.Images.ImageColumns._ID,
        )
        //val collectionUri = Uri.parse("${getCollectionUri()}/$path")
        val collectionUri = getCollectionUri()
        context.contentResolver.query(
            collectionUri,
            projections,
            null, null, null
        )?.use { cursor ->
            cursor.moveToFirst()
            val imageUriIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
            while (!cursor.isAfterLast) {
                val uriString = cursor.getString(imageUriIndex)
                uriList.add(
                    Uri.parse("$collectionUri/$uriString")
                )
                cursor.moveToNext()
            }
            cursor.close()
        }
        return uriList
    }

    private fun getCollectionUri(): Uri {
        val collectionUri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        return collectionUri
    }

}