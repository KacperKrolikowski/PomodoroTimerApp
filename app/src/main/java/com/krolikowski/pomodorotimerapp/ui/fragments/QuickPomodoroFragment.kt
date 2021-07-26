package com.krolikowski.pomodorotimerapp.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.krolikowski.pomodorotimerapp.R
import com.krolikowski.pomodorotimerapp.data.repositories.PomodoroTimerRepository
import com.krolikowski.pomodorotimerapp.ui.viewmodels.PomodoroTimerViewModel
import com.krolikowski.pomodorotimerapp.ui.viewmodels.QuickPomodoroViewModel
import kotlinx.android.synthetic.main.fragment_quick_pomodoro.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuickPomodoroFragment: Fragment(R.layout.fragment_quick_pomodoro) {

    enum class PomodoroTimerState{
        Stopped, Running, Paused
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerSecondsRemaining = 0L
    private var timerState = "Stopped"

    private lateinit var preferenceViewModel: QuickPomodoroViewModel
    private lateinit var pomodoroTimerViewModel: PomodoroTimerViewModel

    object qtPomodoro{
        var qpTime = 0
        var qpQuantity = 0
        var qpSecondsRemaining = 0L
        var qpTimerState: String = "Running"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pomodoroTimerViewModel = ViewModelProvider(this).get(PomodoroTimerViewModel::class.java)
        preferenceViewModel = ViewModelProvider(this).get(QuickPomodoroViewModel::class.java)
        getPreferences()

    }

    override fun onPause() {
        super.onPause()

        if(timerState == "Running"){
            timer.cancel()
        } else if (timerState == "Paused"){

        }

        pomodoroTimerViewModel.saveTimerPreviousLength(timerLengthSeconds)
        pomodoroTimerViewModel.saveTimerSecondsRemaining(timerSecondsRemaining)
        pomodoroTimerViewModel.saveTimerState(timerState)

    }

    override fun onResume() {
        super.onResume()

        initTimer()
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

        timerState = "Running"

        Snackbar.make(requireView(), qtPomodoro.qpTime.toString(), Snackbar.LENGTH_SHORT)
            .show()

    }

    private fun pausePomodoro(){

        timerState = "Paused"
        timer.cancel()

    }

    private fun stopPomodoro(){

        timerState = "Stopped"
        timer.cancel()


    }

    private fun breakPomodoro(){

    }

    private fun initTimer(){
        pomodoroTimerViewModel.readFromTimerDataStoreTimerState.observe(this, {
            qtPomodoro.qpTimerState = it
            Log.d("DEBUG", qtPomodoro.qpTimerState)
            if (qtPomodoro.qpTimerState == "Stopped"){
                //setNewTimer()
            } else{
                //setPreviousTimer()
            }

        })

        lifecycleScope.launch(Dispatchers.Main) {
            val state = pomodoroTimerViewModel.getState()

            Log.d("DEBUGG", state)
        }

    }

}