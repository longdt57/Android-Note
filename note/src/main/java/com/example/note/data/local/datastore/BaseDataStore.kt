package com.example.note.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

abstract class BaseDataStore(
    context: Context,
    prefName: String,
) {

    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile(prefName) }
    )

    protected fun <T> getValue(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map { preferences ->
            preferences[key]
        }
    }

    protected suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        dataStore.edit { settings ->
            settings[key] = value
        }
    }

    protected fun <T> getListValue(key: Preferences.Key<String>): Flow<List<T>> {
        return dataStore.data.map { preferences ->
            val value = preferences[key] ?: return@map emptyList()
            Json.decodeFromString<List<T>>(value)
        }
    }

    protected suspend fun <T> setValue(key: Preferences.Key<String>, value: List<T>) {
        dataStore.edit { settings ->
            val jsonValue = Json.encodeToString(value)
            settings[key] = jsonValue
        }
    }
}
