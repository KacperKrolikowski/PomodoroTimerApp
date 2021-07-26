package com.krolikowski.pomodorotimerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.krolikowski.pomodorotimerapp.data.repositories.PomodoroTimerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PomodoroTimerViewModel(application: Application): AndroidViewModel(application) {

    private val repository = PomodoroTimerRepository(application)

    val readFromTimerDataStoreTimerPreviousLength = repository.readTimerPreviousLength.asLiveData()
    val readFromTimerDataStoreTimerState = repository.readTimerState.asLiveData()
    val readFromTimerDataStoreTimerSecondsRemaining = repository.readTimerSecondsRemaining.asLiveData()

    fun saveToTimerDataStore(timerPreviousLength: Long, timerState: String, timerSecondsRemaining: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveTimerDataToDataStore(timerPreviousLength, timerState, timerSecondsRemaining)
    }

    fun saveTimerPreviousLength(timerPreviousLength: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveTimerPreviousLength(timerPreviousLength)
    }
}