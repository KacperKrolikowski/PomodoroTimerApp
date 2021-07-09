package com.krolikowski.pomodorotimerapp.data.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

const val PREFERENCE_NAME = "quick_pomodoro_settings"

class DataStoreRepository(context: Context) {

    private object PreferencesKeys{
        val time = preferencesKey<String>("quick_pomodoro_time")
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCE_NAME
    )

    suspend fun saveToDataStore(time: String){
        dataStore.edit { preference ->
            preference[PreferencesKeys.time] = time
        }
    }

    val readFromDataStore: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                Log.d("DEBUG_DATASTORE", exception.message.toString())
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences ->
            val quickPomodoroTime = preferences[PreferencesKeys.time] ?: "25"
            quickPomodoroTime
        }


}