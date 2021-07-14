package com.krolikowski.pomodorotimerapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.krolikowski.pomodorotimerapp.R
import com.krolikowski.pomodorotimerapp.ui.viewmodels.QuickPomodoroViewModel
import kotlinx.android.synthetic.main.fragment_preferences.*
import kotlinx.android.synthetic.main.fragment_quick_pomodoro.*

class QuickPomodoroFragment: Fragment(R.layout.fragment_quick_pomodoro) {

    private lateinit var preferenceViewModel: QuickPomodoroViewModel

    object qtPomodoro{
        var qpTime = 0
        var qpQuantity = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceViewModel = ViewModelProvider(this).get(QuickPomodoroViewModel::class.java)
        getPreferences()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pomodoro_name_TV.text = "Quick Pomodoro"

        start_pause_button.setOnClickListener {
            pomodoroTimer()
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

    private fun pomodoroTimer(){

    }

    private fun startPomodoro(){

    }

    private fun pausePomodoro(){

    }

    private fun stopPomodoro(){

    }

    private fun breakPomodoro(){

    }

}