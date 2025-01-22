package com.example.note.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDataStore @Inject constructor(
    context: Context,
) : BaseDataStore(context, NOTE_DATASTORE) {

    fun hasSyncedFromCloud(): Flow<Boolean> {
        return getValue(KEY_HAS_SYNCED_FROM_CLOUD).map { it ?: false }
    }

    suspend fun setHasSyncedFromCloud(hasSynced: Boolean) {
        setValue(KEY_HAS_SYNCED_FROM_CLOUD, hasSynced)
    }

    fun lastTimeSyncedToCloud(): Flow<Long> {
        return getValue(KEY_LAST_TIME_SYNCED_TO_CLOUD).map { it ?: 0 }
    }

    suspend fun setLastTimeSyncedToCloud(hasSynced: Long) {
        setValue(KEY_LAST_TIME_SYNCED_TO_CLOUD, hasSynced)
    }

    companion object {
        private const val NOTE_DATASTORE = "note-datastore"

        private val KEY_HAS_SYNCED_FROM_CLOUD = booleanPreferencesKey("KEY_HAS_SYNCED_FROM_CLOUD")
        private val KEY_LAST_TIME_SYNCED_TO_CLOUD =
            longPreferencesKey("KEY_LAST_TIME_SYNCED_TO_CLOUD")
    }
}