package com.krolikowski.pomodorotimerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.krolikowski.pomodorotimerapp.data.repositories.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuickPomodoroViewModel(application: Application): AndroidViewModel(application) {

    private val repository = DataStoreRepository(application)

    val readFromDataStore = repository.readFromDataStore.asLiveData()

    fun saveToDataStore(time: String) = viewModelScope.launch(Dispatchers.IO){
        repository.saveToDataStore(time)
    }
}