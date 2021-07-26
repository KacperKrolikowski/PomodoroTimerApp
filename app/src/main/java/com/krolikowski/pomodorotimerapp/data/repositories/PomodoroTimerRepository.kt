package com.krolikowski.pomodorotimerapp.data.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

const val PREFERENCE_TIMER_NAME = "timer_data"

class PomodoroTimerRepository(context: Context) {

    private object TimerKeys{
        val timerPreviousLength = preferencesKey<Long>("timer_previous")
        val timerState = preferencesKey<String>("timer_state")
        val timerSecondsRemaining = preferencesKey<String>("timer_seconds_remaining")
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCE_TIMER_NAME
    )

    //Timer
    suspend fun saveTimerDataToDataStore(timerPreviousLength: Long, timerState: String, timerSecondsRemaining: String){
        dataStore.edit { preference ->
            preference[TimerKeys.timerPreviousLength] = timerPreviousLength
            preference[TimerKeys.timerState] = timerState
            preference[TimerKeys.timerSecondsRemaining] = timerSecondsRemaining
        }
    }

    suspend fun saveTimerPreviousLength(timerPreviousLength: Long){
        dataStore.edit { preference ->
            preference[TimerKeys.timerPreviousLength] = timerPreviousLength
        }
    }

    val readTimerPreviousLength: Flow<Long> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                Log.d("DEBUG_DATASTORE", exception.message.toString())
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences ->
            val timerPreviousLength = preferences[TimerKeys.timerPreviousLength] ?: 0L
            timerPreviousLength
        }

    val readTimerState: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                Log.d("DEBUG_DATASTORE", exception.message.toString())
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences ->
            val timerState = preferences[TimerKeys.timerState] ?: "25"
            timerState
        }

    val readTimerSecondsRemaining: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                Log.d("DEBUG_DATASTORE", exception.message.toString())
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences ->
            val timerSecondsRemaining = preferences[TimerKeys.timerSecondsRemaining] ?: "25"
            timerSecondsRemaining
        }

}