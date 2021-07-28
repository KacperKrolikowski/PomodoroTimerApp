package com.krolikowski.pomodorotimerapp.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.krolikowski.pomodorotimerapp.others.TimerExpiredReceiver
import com.krolikowski.pomodorotimerapp.ui.viewmodels.PomodoroTimerViewModel
import com.krolikowski.pomodorotimerapp.ui.viewmodels.QuickPomodoroViewModel
import kotlinx.android.synthetic.main.fragment_quick_pomodoro.*
import kotlinx.coroutines.*
import java.util.*

class QuickPomodoroFragment: Fragment(R.layout.fragment_quick_pomodoro) {

    private val deb_pom = "DEBUG_POMODORO"

    private lateinit var preferenceViewModel: QuickPomodoroViewModel
    private lateinit var pomodoroTimerViewModel: PomodoroTimerViewModel

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

    override fun onPause() {
        super.onPause()

        if(timerState == "Running"){
            timer.cancel()
            val wakeUpTime = setAlarm(requireContext(), nowSeconds, timerSecondsRemaining)
        } else if (timerState == "Paused"){

        }

        pomodoroTimerViewModel.saveTimerPreviousLength(timerLengthSeconds)
        pomodoroTimerViewModel.saveTimerSecondsRemaining(timerSecondsRemaining)
        pomodoroTimerViewModel.saveTimerState(timerState)

    }

    override fun onResume() {
        super.onResume()

        initTimer()
        updateButtons()
        removeAlarm(requireContext())

    }

    private fun getPreferences(){
        preferenceViewModel.readFromDataStoreTime.observe(this, {
            timerLengthSeconds = it.toLong()
            Log.d("DEBUG",it)
        })

        preferenceViewModel.readFromDataStoreQuantity.observe(this, {
            timerQuantity = it.toInt()
            Log.d("DEBUG2",it)

        })
    }

    private fun initTimer(){

        lifecycleScope.launch(Dispatchers.Main) {
            timerState = pomodoroTimerViewModel.getTimerState()

            Log.d(deb_pom, timerState)

            if (timerState == "Stopped"){
                setNewTimer()
            } else{
                setPreviousTimer()
            }

            timerSecondsRemaining = if(timerState == "Running" || timerState == "Paused"){
                pomodoroTimerViewModel.getTimerSecondsRemaining()
            }else{
                timerLengthSeconds
            }

            val alarmSetTime = pomodoroTimerViewModel.getTimerAlarmTime()
            if (alarmSetTime > 0){
                timerSecondsRemaining -= nowSeconds - alarmSetTime
            }

            if (timerSecondsRemaining <= 0){
                onTimerFinished()
            } else if (timerState == "Running"){
                startPomodoro()
            }
            updateButtons()
            updateUI()
        }

    }

    private fun startPomodoro(){

        timerState = "Running"

        timer = object : CountDownTimer(timerSecondsRemaining * 1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                timerSecondsRemaining = millisUntilFinished / 1000
                updateUI()
            }

            override fun onFinish() = onTimerFinished()
        }.start()
        updateButtons()

    }

    private fun setNewTimer(){
        lifecycleScope.launch(Dispatchers.Main) {
            val lengthInMinutes = preferenceViewModel.readQuickPomodoroTime().toLong()
            timerLengthSeconds = (lengthInMinutes * 60L)
        }
    }

    private fun setPreviousTimer(){

        lifecycleScope.launch(Dispatchers.Main){
            timerLengthSeconds = pomodoroTimerViewModel.getTimerPreviousLength()
        }

    }

    private fun pausePomodoro(){

        timerState = "Paused"

        timer.cancel()
        updateButtons()

    }

    private fun stopPomodoro(){

        timerState = "Stopped"
        updateButtons()
        timer.cancel()


    }

    private fun breakPomodoro(){

    }

    private fun onTimerFinished(){
        timerState = "Stopped"

        //setNewTimer()

    }

    private fun updateUI(){
        val minutesUntilFinished = timerSecondsRemaining/60
        val secondsInMinuteUntilFinished = timerSecondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        main_timer.text =
            "$minutesUntilFinished:${
            if (secondsStr.length == 2) 
                secondsStr
            else "0" + secondsStr}"
    }

    private fun updateButtons(){
        when(timerState){
            "Running" ->{
                start_fab.isEnabled = false
                stop_fab.isEnabled = true
                pause_fab.isEnabled = true
            }
            "Stopped" ->{
                start_fab.isEnabled = true
                stop_fab.isEnabled = false
                pause_fab.isEnabled = false
            }
            "Paused" ->{
                start_fab.isEnabled = true
                stop_fab.isEnabled = true
                pause_fab.isEnabled = false
            }
        }
    }

    fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long{
        val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, TimerExpiredReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
        lifecycleScope.launch(Dispatchers.IO){
            pomodoroTimerViewModel.saveTimerAlarmTime(nowSeconds)
        }
        return wakeUpTime
    }

    fun removeAlarm(context: Context){
        val intent = Intent(context, TimerExpiredReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        lifecycleScope.launch(Dispatchers.IO){
            pomodoroTimerViewModel.saveTimerAlarmTime(0)
        }
    }

    val nowSeconds: Long
        get() = Calendar.getInstance().timeInMillis / 1000

}