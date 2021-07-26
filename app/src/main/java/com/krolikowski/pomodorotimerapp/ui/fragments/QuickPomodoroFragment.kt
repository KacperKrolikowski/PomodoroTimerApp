package com.krolikowski.pomodorotimerapp.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.krolikowski.pomodorotimerapp.R
import com.krolikowski.pomodorotimerapp.ui.viewmodels.PomodoroTimerViewModel
import com.krolikowski.pomodorotimerapp.ui.viewmodels.QuickPomodoroViewModel
import kotlinx.android.synthetic.main.fragment_quick_pomodoro.*

class QuickPomodoroFragment: Fragment(R.layout.fragment_quick_pomodoro) {

    enum class PomodoroTimerState{
        Stopped, Running, Paused
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = PomodoroTimerState.Stopped

    private lateinit var preferenceViewModel: QuickPomodoroViewModel
    private lateinit var pomodoroTimerViewModel: PomodoroTimerViewModel

    object qtPomodoro{
        var qpTime = 0
        var qpQuantity = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceViewModel = ViewModelProvider(this).get(QuickPomodoroViewModel::class.java)
        getPreferences()

    }

    override fun onPause() {
        super.onPause()

        if(timerState == PomodoroTimerState.Running){
            timer.cancel()
        } else if (timerState == PomodoroTimerState.Paused){

        }

        pomodoroTimerViewModel.saveTimerPreviousLength(timerLengthSeconds)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pomodoro_name_TV.text = "Quick Pomodoro"


        start_fab.setOnClickListener {
            startPomodoro()

        }

        pause_fab.setOnClickListener {
            pausePomodoro()
        }

        stop_fab.setOnClickListener {
            stopPomodoro()
        }

    }

    private fun getPreferences(){
        preferenceViewModel.readFromDataStoreTime.observe(this, {
            qtPomodoro.qpTime = it.toInt()
            Log.d("DEBUG",it)
        })

        preferenceViewModel.readFromDataStoreQuantity.observe(this, {
            qtPomodoro.qpQuantity = it.toInt()
            Log.d("DEBUG2",it)

        })
    }


    private fun startPomodoro(){

        timerState = PomodoroTimerState.Running

        Snackbar.make(requireView(), qtPomodoro.qpTime.toString(), Snackbar.LENGTH_SHORT)
            .show()

    }

    private fun pausePomodoro(){

        timerState = PomodoroTimerState.Paused
        timer.cancel()

    }

    private fun stopPomodoro(){

        timerState = PomodoroTimerState.Stopped
        timer.cancel()


    }

    private fun breakPomodoro(){

    }

}