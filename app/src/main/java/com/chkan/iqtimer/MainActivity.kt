package com.chkan.iqtimer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.chkan.iqtimer.domain.TimerService
import com.chkan.iqtimer.domain.models.Session
import com.chkan.iqtimer.ui.progress.GoalBottomFragment
import com.chkan.iqtimer.utils.SET_UPD_BREAK
import com.chkan.iqtimer.utils.SET_UPD_TIME
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mService: TimerService
    private var isBound: Boolean = false
    private var isStarted: Boolean = false

    @Inject
    lateinit var session: Session

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.LocalBinder
            mService = binder.getService()
            isBound = true
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        session.settingsUpdatedLiveData.observe(this){
            when(it.first) {
                SET_UPD_TIME -> { updateTimer(it.second) }
                SET_UPD_BREAK -> { updateBreak(it.second) }
            }

        }
    }

    fun startTimer(){
      if(isBound){mService.startTimer()}
      if(!isStarted){startTimerService()}
    }

    private fun startTimerService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, TimerService::class.java))
        } else {
            startService(Intent(this, TimerService::class.java))
        }
        isStarted = true
    }

    fun stopTimer(isPause:Boolean){
        if(isBound){
            mService.stopTimer(isPause)
        }
    }

    private fun updateTimer(time: String) {
        if(isBound){
            mService.updateTimer(time)
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }

    private fun updateBreak(time: String) {
        if(isBound){
            mService.updateBreak(time)
        }
    }

    fun getBottomSheet() {
        GoalBottomFragment().apply {
            show(supportFragmentManager,"TAG_SHEET")
        }
    }

}