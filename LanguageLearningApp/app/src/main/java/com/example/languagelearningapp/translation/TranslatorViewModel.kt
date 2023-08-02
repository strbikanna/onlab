package com.example.languagelearningapp.translation


import android.util.Log
import android.util.LruCache
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TranslatorViewModel @Inject constructor() : ViewModel() {

    var sourceLanguage: Language = Language(TranslateLanguage.ENGLISH)
        set(value) {
            if (!existsLanguage(value)) return
            field = value
            downloadLanguage(value)
        }
    var targetLanguage: Language = Language(TranslateLanguage.HUNGARIAN)
        set(value) {
            if (!existsLanguage(value)) return
            field = value
            downloadLanguage(value)
        }
    private val modelManager: RemoteModelManager = RemoteModelManager.getInstance()
    private val pendingDownloads: HashMap<String, Task<Void>> = hashMapOf()

    companion object {
        private const val TRANSLATOR_COUNT = 3
    }

    val availableLanguages: List<Language> =
        TranslateLanguage.getAllLanguages().map { Language(it) }

    val sourceText = String()
    val translatedText = MutableLiveData<ResultOrError>()
    private val availableModels = MutableLiveData<List<String>>()
    private val translators =
        object : LruCache<TranslatorOptions, Translator>(TRANSLATOR_COUNT) {
            override fun create(options: TranslatorOptions): Translator {
                return Translation.getClient(options)
            }

            override fun entryRemoved(
                evicted: Boolean,
                key: TranslatorOptions,
                oldValue: Translator,
                newValue: Translator?,
            ) {
                oldValue.close()
            }
        }

    data class ResultOrError(var result: String?, var error: Exception?)


    init {
        downloadLanguage(sourceLanguage)
        downloadLanguage(targetLanguage)
        fetchDownloadedModels()
    }

    private fun fetchDownloadedModels() {
        modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { remoteModels ->
                availableModels.value = remoteModels.sortedBy { it.language }.map { it.language }
                Log.d("Translator", "Available models fetched.")
            }
    }

    fun existsLanguage(language: Language): Boolean {
        return TranslateLanguage.fromLanguageTag(language.languageCode) != null
    }

    private fun downloadLanguage(language: Language) {
        if (!existsLanguage(language)) return
        val model = getModel(TranslateLanguage.fromLanguageTag(language.languageCode)!!)
        var downloadTask: Task<Void>?
        if (pendingDownloads.containsKey(language.languageCode)) {
            downloadTask = pendingDownloads[language.languageCode]

            if (downloadTask != null && !downloadTask.isCanceled) {
                return
            }
        }
        downloadTask =                                      //requireWifi()
            modelManager.download(model, DownloadConditions.Builder().build())
                .addOnCompleteListener {
                    Log.d("Translator", "Download for language: ${language.displayName} completed.")
                    synchronized(this) {
                        pendingDownloads.remove(language.languageCode)
                        fetchDownloadedModels()
                    }
                }
        Log.d("Translator", "Language ${language.displayName} added to pending download list.")
        pendingDownloads[language.languageCode] = downloadTask
    }

    private fun getModel(languageCode: String): TranslateRemoteModel {
        return TranslateRemoteModel.Builder(languageCode).build()
    }

    fun requiresModelDownload(
        lang: Language,
    ): Boolean {
        val downloadedModels = availableModels.value
        return if (downloadedModels == null) {
            !pendingDownloads.containsKey(lang.languageCode)
        } else !downloadedModels.contains(lang.languageCode) && !pendingDownloads.containsKey(lang.languageCode)
    }

    private val translationCompletedListener =
        OnCompleteListener { task ->
            Log.d("Translator", "Translation completed, result: ${translatedText.value?.result}")
            if (task.isSuccessful) {
                translatedText.value = ResultOrError(task.result, null)
            } else {
                translatedText.value = ResultOrError(null, task.exception)
            }
        }

    fun translate(text: String = sourceText) {
        Log.d("Translator", "Translation for $text started.")

        if (text.isEmpty()) {
            return
        }
        val sourceLangCode = TranslateLanguage.fromLanguageTag(sourceLanguage.languageCode)!!
        val targetLangCode = TranslateLanguage.fromLanguageTag(targetLanguage.languageCode)!!
        val options =
            TranslatorOptions.Builder()
                .setSourceLanguage(sourceLangCode)
                .setTargetLanguage(targetLangCode)
                .build()
        translators[options]
            .downloadModelIfNeeded().continueWithTask { task ->
                if (task.isSuccessful) {
                    translators[options].translate(text)
                        .addOnCompleteListener(translationCompletedListener)
                } else {
                    Tasks.forException(task.exception ?: Exception("Unknown translation error."))
                }
            }
    }

}

