package com.krolikowski.pomodorotimerapp.data.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import androidx.lifecycle.LiveData
import com.krolikowski.pomodorotimerapp.data.db.entities.SinglePomodoro
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

const val PREFERENCE_NAME = "quick_pomodoro_settings"

class DataStoreRepository(context: Context) {

    private object PreferencesKeys{
        val time = preferencesKey<String>("quick_pomodoro_time")
        val quantity = preferencesKey<String>("quick_pomodoro_quantity")
        val language = preferencesKey<String>("pomodoro_app_language")
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCE_NAME
    )

    suspend fun saveToDataStore(time: String, quantity: String, language: String){
        dataStore.edit { preference ->
            preference[PreferencesKeys.time] = time
            preference[PreferencesKeys.quantity] = quantity
            preference[PreferencesKeys.language] = language
        }
    }

    val readQuickPomodoroTime: Flow<String> = dataStore.data
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

    val readQuickPomodoroQuantity: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                Log.d("DEBUG_DATASTORE", exception.message.toString())
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences ->
            val quickPomodoroQuantity = preferences[PreferencesKeys.quantity] ?: "4"
            quickPomodoroQuantity
        }

    val readApplicationLanguage: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                Log.d("DEBUG_DATASTORE", exception.message.toString())
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences ->
            val applicationLanguage = preferences[PreferencesKeys.language] ?: "English"
            applicationLanguage
        }

}