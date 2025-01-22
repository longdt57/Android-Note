package com.example.note.di

import android.content.Context
import androidx.room.Room
import com.example.note.data.local.room.NoteRoomDatabase
import com.example.note.data.local.room.daos.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private const val NOTE_DATABASE = "note-database"

@Module
@InstallIn(SingletonComponent::class)
internal class NoteRoomModule {

    @Provides
    fun provideRoom(@ApplicationContext applicationContext: Context): NoteRoomDatabase {
        return Room.databaseBuilder(
            applicationContext,
            NoteRoomDatabase::class.java,
            NOTE_DATABASE
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideNoteDao(database: NoteRoomDatabase): NoteDao = database.noteDao()

}