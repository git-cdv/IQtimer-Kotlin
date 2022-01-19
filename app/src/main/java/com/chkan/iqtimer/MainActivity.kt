package com.chkan.iqtimer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.chkan.iqtimer.domain.TimerService
import com.chkan.iqtimer.domain.usecases.AchievementsUseCase
import com.chkan.iqtimer.domain.usecases.SessionsUseCase
import com.chkan.iqtimer.ui.progress.GoalBottomFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sessionsUseCase: SessionsUseCase
    @Inject
    lateinit var achievUseCase: AchievementsUseCase

    private lateinit var mService: TimerService
    private var isBound: Boolean = false
    private var isStarted: Boolean = false

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

        lifecycleScope.launch (Dispatchers.IO) {
            if(sessionsUseCase.isFirst()){
                sessionsUseCase.startFirst()
                achievUseCase.initAchievements()
                Log.d("MYAPP", "startFirst() - DONE")
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

    fun updateTimer(){
        if(isBound){
            mService.updateTimer()
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                sessionsUseCase.checkWorkDate()
            } catch (e:Exception){
                Log.d("MYAPP", "onStart() - Exception: ${e.message}")
            }

        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }

    fun updateBreak() {
        if(isBound){
            mService.updateBreak()
        }
    }

    fun getBottomSheet() {
        GoalBottomFragment().apply {
            show(supportFragmentManager,"TAG_SHEET")
        }
    }
}