package com.example.note.data.local.room.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.note.data.models.NoteEntity
import com.example.note.data.models.NoteStatus

@Dao
interface NoteDao {

    @Query(
        """
        SELECT * FROM notes
        WHERE LOWER(content) LIKE '%' || LOWER(:query) || '%'
        AND timestamp < :fromDate
        ORDER BY timestamp DESC
        LIMIT :limit
        """
    )
    suspend fun getNotes(query: String, limit: Int, fromDate: Long): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE synced_cloud = 0")
    suspend fun getUnSyncedNotes(): List<NoteEntity>

    @Upsert
    suspend fun upsert(note: NoteEntity): Long

    @Upsert
    suspend fun upsertItems(notes: List<NoteEntity>)

    @Query("UPDATE notes SET status = '${NoteStatus.DELETE}', synced_cloud = 0 WHERE id = :id")
    suspend fun markAsDeletedById(id: Long)

    @Query("DELETE FROM notes WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>)
}