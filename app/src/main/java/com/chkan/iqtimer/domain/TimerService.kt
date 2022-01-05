package com.chkan.iqtimer.domain

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class TimerService : Service() {

    @Inject
    lateinit var session: Session

    companion object {
        var leftInMillis: Long = 0
        var timerObject: Timer? = null
    }

    override fun onCreate() {
        super.onCreate()
        leftInMillis = session.timeDefault.get()?.toLong()!!*60000
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        return START_NOT_STICKY
    }

    fun startTimer(){
        timerObject = Timer(leftInMillis,1000)
        timerObject?.start()
    }

    fun stopTimer(isPause:Boolean){
        timerObject?.cancel()
        timerObject = null
        if (!isPause){leftInMillis = session.timeDefault.get()?.toLong()!!*60000 }
    }

    inner class Timer(millisInFuture: Long, interval: Long) : CountDownTimer(
        millisInFuture, interval) {

        var time: String = ""
        var seconds: Long =0

        override fun onTick(millisUntilFinished: Long) {
            leftInMillis = millisUntilFinished
            seconds = leftInMillis/1000

            time = if (leftInMillis >= 3600000) { //если время отчета равно или больше 1 часа, то формат с часами
                String.format(
                    Locale.getDefault(), "%02d:%02d:%02d", seconds / 3600,
                    seconds % 3600 / 60, seconds % 60
                )
            } else { //формат с минутами и секундами
                   String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60)
            }
            session.timeLiveData.value = time
        }

        override fun onFinish() {

        }
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun updateTimer() {
        leftInMillis = session.timeDefault.get()?.toLong()!!*60000
    }
}