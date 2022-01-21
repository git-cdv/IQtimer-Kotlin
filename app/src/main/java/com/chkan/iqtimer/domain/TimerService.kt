package com.chkan.iqtimer.domain

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import com.chkan.iqtimer.domain.models.Session
import com.chkan.iqtimer.ui.main.NotifManager
import com.chkan.iqtimer.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class TimerService : Service() {

    @Inject lateinit var session: Session
    @Inject lateinit var notifManager: NotifManager

    companion object {
        var leftInMillis: Long = 0
        var breakInMillis: Long = 0
        var timerObject: Timer? = null
        var isBreak = false
    }

    override fun onCreate() {
        super.onCreate()
        leftInMillis = session.timeDefault.toLong()*60000
        breakInMillis = session.breakDefault.toLong()*60000
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val command = intent.getIntExtra(KEY_COMMAND, 0)
        Log.d("MYAPP", "onStartCommand - command: $command")

        when(command){
            COM_RUN_NOTIF -> {
                if(session.stateLiveData.value != State.PAUSED) {
                    leftInMillis = session.timeDefault.toLong() * 60000
                }
                startTimer()
                session.stateLiveData.value = State.ACTIVE
            }
            COM_PAUSE_NOTIF -> {
                stopTimer(true)
                session.stateLiveData.value = State.PAUSED
            }
            COM_STOP_NOTIF -> {
                stopTimer(false)
                session.stateLiveData.value = State.STOPED
            }
            COM_BREAK_NOTIF -> {
                breakTimer()
                session.stateLiveData.value = State.BREAK
            }
        }
        return START_NOT_STICKY
    }

    fun startTimer(){
        timerObject = Timer(leftInMillis,1000)
        timerObject?.start()
    }

    fun stopTimer(isPause:Boolean){
        timerObject?.cancel()
        timerObject = null
        isBreak = false
        if (!isPause){
            leftInMillis = session.timeDefault.toLong()*60000
            stopForeground(true) //отключаем нотификацию
        } else {
            startForeground(1, notifManager.onPause())
        }
    }

    fun breakTimer() {
        isBreak = true
        timerObject = Timer(breakInMillis,1000)
        timerObject?.start()
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

            if (isBreak) {
                startForeground(1, notifManager.onBreak(time))
            } else {
                startForeground(1, notifManager.onRun(time))
            }
        }

        override fun onFinish() {
            Log.d("MYAPP", "onFinish() - isBreak: $isBreak")
            if (isBreak) {
                stopMyForegroud(notifManager.onBreakEnd())
                isBreak = false
            }else {
                try {
                stopMyForegroud(notifManager.onEnd())
                session.addDoneSession()
                } catch (e:Exception){
                    Log.d("MYAPP", "onFinish() - Exception: ${e.message}")
                }

            }
            session.stateLiveData.value = State.STOPED
            stopTimer(false)
        }
    }

    private fun stopMyForegroud(notif: Notification) {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_DETACH)
        } else {
            stopForeground(false)
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notif)
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun updateTimer() {
        leftInMillis = session.timeDefault.toLong()*60000
    }

    fun updateBreak() {
        breakInMillis = session.breakDefault.toLong()*60000
    }
}