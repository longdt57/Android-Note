package com.example.note.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.note.data.local.room.daos.NoteDao
import com.example.note.data.models.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
internal abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}