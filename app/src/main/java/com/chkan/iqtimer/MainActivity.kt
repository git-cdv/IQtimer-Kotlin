package com.chkan.iqtimer

import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.chkan.iqtimer.domain.TimerService
import com.chkan.iqtimer.domain.models.Session
import com.chkan.iqtimer.ui.progress.GoalBottomFragment
import com.chkan.iqtimer.utils.*
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

        session.stateLiveData.observe(this) {
            when (it) {
                State.TIMER_ENDED -> { showDialog(
                    title = getString(R.string.dialog_session_end),
                    text = getString(R.string.qest_break),
                    positiveText = getString(R.string.dialog_rest_start),
                    positive = {
                        startBreak()
                    },
                    negative = {
                        session.stateLiveData.value = State.STOPED
                    }
                )}
                State.BREAK_ENDED -> { showDialog(
                    title = getString(R.string.dialog_break_end),
                    text = getString(R.string.qest_continue),
                    positiveText = getString(R.string.work_start),
                    positive = {
                        startTimer()
                    },
                    negative = {
                        session.stateLiveData.value = State.STOPED
                    }
                )}
                else -> {}
            }
        }
    }

    fun startTimer(){
      if(isBound){mService.startTimer()}
      if(!isStarted){startTimerService()}
    }

    private fun startBreak(){
        if(isBound){mService.startBreak()}
        if(!isStarted){startTimerService(true)}
    }

    private fun startTimerService(isBreak:Boolean=false) {
        val intent = Intent(this, TimerService::class.java)
        if (isBreak) intent.putExtra(KEY_COMMAND, COM_BREAK_NOTIF)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
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

    private fun showDialog(title:String, text:String, positiveText:String, positive: (()-> Unit), negative: (()-> Unit)) {

        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle(title)
            setMessage(text)
            setPositiveButton(positiveText) { _, _ -> positive.invoke() }
            setNegativeButton(getString(R.string.cancel)) { _, _ -> negative.invoke() }
            setOnCancelListener { negative.invoke() }
            show()
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