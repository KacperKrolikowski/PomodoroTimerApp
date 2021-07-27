package com.krolikowski.pomodorotimerapp.others

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.krolikowski.pomodorotimerapp.ui.fragments.QuickPomodoroFragment
import com.krolikowski.pomodorotimerapp.ui.viewmodels.PomodoroTimerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TimerExpiredReceiver : BroadcastReceiver() {

    private lateinit var pomodoroTimerViewModel: PomodoroTimerViewModel

    override fun onReceive(context: Context, intent: Intent) {
        pomodoroTimerViewModel = PomodoroTimerViewModel(Application())
        GlobalScope.launch {
            pomodoroTimerViewModel.saveTimerState("Stopped")
            pomodoroTimerViewModel.saveTimerAlarmTime(0)
        }


    }
}