package com.krolikowski.pomodorotimerapp.ui.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.krolikowski.pomodorotimerapp.R
import com.krolikowski.pomodorotimerapp.others.TimerService
import com.krolikowski.pomodorotimerapp.ui.viewmodels.PomodoroTimerViewModel
import com.krolikowski.pomodorotimerapp.ui.viewmodels.QuickPomodoroViewModel
import kotlinx.android.synthetic.main.fragment_quick_pomodoro.*

class QuickPomodoroFragment: Fragment(R.layout.fragment_quick_pomodoro) {

    private val deb_pom = "DEBUG_POMODORO"

    private lateinit var preferenceViewModel: QuickPomodoroViewModel
    private lateinit var pomodoroTimerViewModel: PomodoroTimerViewModel

    private lateinit var serviceIntent: Intent

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerQuantity = 0
    private var timerSecondsRemaining = 0L
    private var timerState = "Stopped"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pomodoroTimerViewModel = PomodoroTimerViewModel(requireContext())
        preferenceViewModel = ViewModelProvider(this).get(QuickPomodoroViewModel::class.java)

        getPreferences()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pomodoro_name_TV.text = "Quick Pomodoro"

        serviceIntent = Intent(requireContext(), TimerService::class.java)
        requireContext().registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATE))

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
            timerLengthSeconds = it.toLong()
            updateUI(timerLengthSeconds)
            Log.d("DEBUG",it)
        })

        preferenceViewModel.readFromDataStoreQuantity.observe(this, {
            timerQuantity = it.toInt()
            Log.d("DEBUG2",it)

        })
    }

    private fun startPomodoro(){

        if(timerState == "Stopped") {
            serviceIntent.putExtra("TIME_TO_COUNT", timerLengthSeconds)
            Log.d("DEBUG2", timerLengthSeconds.toString())
            requireContext().startService(serviceIntent)
            updateButtons()
            timerState = "Running"
        } else if (timerState == "Paused"){

        }

    }

    private fun pausePomodoro(){

        timerState = "Paused"
        requireContext().stopService(serviceIntent)
        updateButtons()

    }

    private fun stopPomodoro(){

        timerState = "Stopped"
        updateButtons()



    }

    private fun breakPomodoro(){

    }

    private fun updateButtons(){
        when(timerState){
            "Running" ->{
                start_fab.isEnabled = false
                stop_fab.isEnabled = true
                stop_fab.isEnabled = true
            }
            "Stopped" ->{
                start_fab.isEnabled = true
                stop_fab.isEnabled = false
                stop_fab.isEnabled = false
            }
            "Paused" ->{
                start_fab.isEnabled = true
                stop_fab.isEnabled = true
                stop_fab.isEnabled = false
            }
        }
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val receivedTime = intent!!.getLongExtra(TimerService.TIME_EXTRA, 0L)
            updateUI(receivedTime)


        }
    }

    private fun updateUI(newTime: Long){
        val minutesUntilFinished = newTime/60
        val secondsInMinuteUntilFinished = newTime - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        main_timer.text =
            "$minutesUntilFinished:${
                if (secondsStr.length == 2)
                    secondsStr
                else "0" + secondsStr}"

    }

}