package com.kiselev.financialcompanion.controller

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class StorageController(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("User")
        val USER_ID_KEY = intPreferencesKey("user_id")
        val AUTH_STATUS_KEY = stringPreferencesKey("auth_status")
        const val AUTHENTICATED_STATUS = "authenticated"
        const val UNAUTHENTICATED_STATUS = "unauthenticated"
    }

    fun getId(): Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY] ?: 0
    }

    suspend fun saveId(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = id
        }
    }

    suspend fun saveAuthenticationStatus(authStatus: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_STATUS_KEY] = authStatus
        }
    }

    suspend fun checkAuthenticationStatus(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[AUTH_STATUS_KEY] == AUTHENTICATED_STATUS
    }

}
