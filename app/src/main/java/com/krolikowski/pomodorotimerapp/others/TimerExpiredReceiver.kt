package com.krolikowski.pomodorotimerapp.others

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.krolikowski.pomodorotimerapp.ui.MainActivity
import com.krolikowski.pomodorotimerapp.ui.fragments.QuickPomodoroFragment
import com.krolikowski.pomodorotimerapp.ui.viewmodels.PomodoroTimerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TimerExpiredReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {




    }
}