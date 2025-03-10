package com.example.jetpackcompose.manager

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "pomodoro_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        private val SELECTED_DAYS_KEY = intPreferencesKey("selected_days")
        private val POMODORO_COUNT_KEY = intPreferencesKey("pomodoro_count")
    }

    val selectedDays: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[SELECTED_DAYS_KEY] ?: 1
    }

    suspend fun saveSelectedDays(days: Int) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_DAYS_KEY] = days
        }
    }

    val pomodoroCount: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[POMODORO_COUNT_KEY] ?: 0
    }

    suspend fun savePomodoroCount(count: Int) {
        context.dataStore.edit { prefs ->
            prefs[POMODORO_COUNT_KEY] = count
        }
    }
}
