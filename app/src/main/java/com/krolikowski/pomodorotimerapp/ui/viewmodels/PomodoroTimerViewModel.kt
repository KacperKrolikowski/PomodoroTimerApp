package com.krolikowski.pomodorotimerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.krolikowski.pomodorotimerapp.data.repositories.PomodoroTimerRepository
import com.krolikowski.pomodorotimerapp.ui.fragments.QuickPomodoroFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PomodoroTimerViewModel(application: Application): AndroidViewModel(application) {

    private val repository = PomodoroTimerRepository(application)

    //Read
    suspend fun getTimerState() = repository.readTimerState()
    suspend fun getTimerSecondsRemaining() = repository.readTimerSecondsRemaining()
    suspend fun getTimerPreviousLength() = repository.readTimerPreviousLength()

    //Save
    fun saveToTimerDataStore(timerPreviousLength: Long, timerState: String, timerSecondsRemaining: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveTimerDataToDataStore(timerPreviousLength, timerState, timerSecondsRemaining)
    }

    fun saveTimerPreviousLength(timerPreviousLength: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveTimerPreviousLength(timerPreviousLength)
    }

    fun saveTimerState(timerState: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveTimerState(timerState)
    }

    fun saveTimerSecondsRemaining(timerSecondsRemaining: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveTimerSecondsRemaining(timerSecondsRemaining)
    }
}