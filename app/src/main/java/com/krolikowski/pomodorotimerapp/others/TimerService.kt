package com.krolikowski.pomodorotimerapp.others

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.krolikowski.pomodorotimerapp.ui.MainActivity
import com.krolikowski.pomodorotimerapp.ui.fragments.QuickPomodoroFragment

class TimerService: Service() {

    private lateinit var timer: CountDownTimer

    private var timerState: String = "Running"
    private var timerSinglePomodoroLength = 25L
    private var timerPomodoroQuantity = 4
    private var timerShortBreakTime = 5L
    private var timerSecondsRemaining = 0L
    private var timeToCount = 0L

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("DEBUG_SERVICE", "here")

        /*
        val notificationIntent = Intent(this, QuickPomodoroFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification = NotificationCompat.Builder(this, "TimerService")
            .setContentTitle("Pomodoro timer")
            .setContentText("00:00")
            .setContentIntent(pendingIntent)
            .build()

         */

        timeToCount = intent!!.getLongExtra("TIME_TO_COUNT", 25L)

        timer = object : CountDownTimer(timeToCount * 1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                timeToCount = millisUntilFinished / 1000
                Log.d("DEBUG_SERVICE", timeToCount.toString())
                val backIntent = Intent(TIMER_UPDATE)
                backIntent.putExtra(TIME_EXTRA, timeToCount)
                sendBroadcast(backIntent)
            }

            override fun onFinish() {

            }
        }.start()
        //startForeground(1, notification)

        return START_NOT_STICKY
    }

    override fun onDestroy() {

        timer.cancel()
        super.onDestroy()
    }

    companion object{
        const val TIMER_UPDATE = "timerUpdate"
        const val TIME_EXTRA = "timeExtra"
    }

}