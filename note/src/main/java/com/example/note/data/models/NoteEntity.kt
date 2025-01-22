package com.example.note.data.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.note.data.models.NoteStatus.Companion.ADD
import com.example.note.data.models.NoteStatus.Companion.DELETE
import com.example.note.data.models.NoteStatus.Companion.UPDATE
import com.example.note.domain.models.NoteD

@Entity(tableName = "notes")
@Keep
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "status") @NoteStatus val status: String = ADD, // Deleted but not sync to cloud
    @ColumnInfo(name = "synced_cloud") val syncedCloud: Boolean = false,
) {
    fun isStatusDelete() = status == DELETE
    fun isStatusAdd() = status == ADD
    fun isStatusUpdate() = status == UPDATE
}

fun NoteEntity.mapToDomainModel() = NoteD(
    id = id,
    content = content,
    timestamp = timestamp
)

fun List<NoteEntity>.mapToDomainModel() = map { it.mapToDomainModel() }