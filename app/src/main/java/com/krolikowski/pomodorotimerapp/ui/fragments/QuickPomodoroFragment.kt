package com.krolikowski.pomodorotimerapp.ui.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.krolikowski.pomodorotimerapp.R
import com.krolikowski.pomodorotimerapp.databinding.FragmentQuickPomodoroBinding
import com.krolikowski.pomodorotimerapp.others.TimerService
import com.krolikowski.pomodorotimerapp.ui.viewmodels.PomodoroTimerViewModel
import com.krolikowski.pomodorotimerapp.ui.viewmodels.QuickPomodoroViewModel
import kotlinx.android.synthetic.main.fragment_quick_pomodoro.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuickPomodoroFragment: Fragment(R.layout.fragment_quick_pomodoro) {

    private val deb_pom = "DEBUG_POMODORO"

    private var _binding: FragmentQuickPomodoroBinding? = null
    private val binding get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuickPomodoroBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pomodoroNameTV.text = "Quick Pomodoro"

        serviceIntent = Intent(requireContext(), TimerService::class.java)
        requireContext().registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATE))

        binding.startFab.setOnClickListener {
            startPomodoro()
        }

        binding.pauseFab.setOnClickListener {
            pausePomodoro()
        }

        binding.stopFab.setOnClickListener {
            stopPomodoro()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unregisterReceiver(updateTime)
        _binding = null
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
            timerState = "Running"
            updateButtons()
        } else if (timerState == "Paused"){
            lifecycleScope.launch(Dispatchers.Main) {
                val previousTime = pomodoroTimerViewModel.getTimerSecondsRemaining()
                serviceIntent.putExtra("TIME_TO_COUNT", previousTime)
                requireContext().startService(serviceIntent)
                timerState = "Running"
                updateButtons()
            }
        }

    }

    private fun pausePomodoro(){

        timerState = "Paused"
        requireContext().registerReceiver(pausedTimer, IntentFilter(TimerService.TIMER_STATE))

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
                binding.startFab.isEnabled = false
                binding.pauseFab.isEnabled = true
                binding.stopFab.isEnabled = true
            }
            "Stopped" ->{
                binding.startFab.isEnabled = true
                binding.pauseFab.isEnabled = false
                binding.stopFab.isEnabled = false
            }
            "Paused" ->{
                binding.startFab.isEnabled = true
                binding.pauseFab.isEnabled = false
                binding.stopFab.isEnabled = true
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

        binding.mainTimer.text =
            "$minutesUntilFinished:${
                if (secondsStr.length == 2)
                    secondsStr
                else "0" + secondsStr}"

    }

    private val pausedTimer: BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val pausedTime = intent!!.getLongExtra(TimerService.TIMER_PAUSED_TIME, 0L)
            lifecycleScope.launch(Dispatchers.IO) {
                pomodoroTimerViewModel.saveTimerSecondsRemaining(pausedTime)
            }
        }
    }

}