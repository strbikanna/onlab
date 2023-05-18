package com.example.languagelearningapp.di

import android.content.Context
import com.example.languagelearningapp.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DIAppModule {
    @Provides
    fun provideDb(
        @ApplicationContext
        context: Context
    ) = AppDatabase.getDatabase(context)

    @Provides
    fun provideWordDao(
        db: AppDatabase
    ) = db.wordDao()

    @Provides
    fun provideDefinitionDao(
        db: AppDatabase
    ) = db.definitionDao()

    @Provides
    fun provideStudyCollectionDao(
        db: AppDatabase
    ) = db.studyCollectionDao()

    @Provides
    fun provideWordCollectionDao(
        db: AppDatabase
    ) = db.wordCollectionDao()

    @Provides
    fun provideWordDefinitionDao(
        db: AppDatabase
    ) = db.wordDefinitionDao()


}