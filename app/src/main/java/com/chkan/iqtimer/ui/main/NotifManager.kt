package com.chkan.iqtimer.ui.main

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.chkan.iqtimer.MainActivity
import com.chkan.iqtimer.R
import com.chkan.iqtimer.domain.TimerService
import com.chkan.iqtimer.utils.*
import javax.inject.Inject

class NotifManager @Inject constructor (private val context: Context, private val notificationManager: NotificationManager ) {

    private val channelProgressId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createProgressChannel() else ""
    private val channelResultId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createSessionChannel() else ""
    private val channelBreakId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createBreakChannel() else ""

    private val pendingToMain = PendingIntent.getActivity(context, 1, Intent(context, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
    private val pendingStop = createPending(2,COM_STOP_NOTIF)
    private val pendingPause = createPending(3,COM_PAUSE_NOTIF)
    private val pendingContinue = createPending(6,COM_RUN_NOTIF)
    private val pendingBreak = createPending(8,COM_BREAK_NOTIF)

    private fun createPending(code:Int, command:Int) : PendingIntent {
        val intent = Intent(context, TimerService::class.java).putExtra(KEY_COMMAND, command)
        return PendingIntent.getService(context, code, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    fun onRun(time: String): Notification {

        return NotificationCompat.Builder(context, channelProgressId)
            .setOngoing(true) //прилипает оповещение и можно удалить только програмно
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(context.getString(R.string.dowork))
            .setContentText(time)
            .setSmallIcon(R.drawable.ic_notif_timer)
            .setContentIntent(pendingToMain)
            .addAction(0, context.getString(R.string.stop), pendingStop)
            .addAction(0, context.getString(R.string.pause), pendingPause)
            .build()
    }

    fun onPause(): Notification {

        return NotificationCompat.Builder(context, channelProgressId)
                .setOngoing(false) //прилипает оповещение и можно удалить только програмно
                .setContentTitle(context.getString(R.string.on_pause))
                .setContentText(context.getString(R.string.qest_continue))
                .setSmallIcon(R.drawable.ic_notif_pause)
                .setContentIntent(pendingToMain)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .addAction(0, context.getString(R.string.stop), pendingStop)
                .addAction(0, context.getString(R.string.dialog_continue), pendingContinue)
                .build()
    }

    fun onEnd(notifSoundOut: Boolean): Notification {

        if(notifSoundOut) playOutNotif()

        return NotificationCompat.Builder(context, channelResultId)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setShowWhen(false)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.dialog_session_end))
                .setContentText(context.getString(R.string.qest_break))
                .setSmallIcon(R.drawable.ic_notif_end)
                .setContentIntent(pendingToMain)
                .addAction(0, context.getString(R.string.dialog_rest_start), pendingBreak)
                .addAction(0, context.getString(R.string.break_skip), pendingContinue)
                .build()
    }

    private fun playOutNotif() {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioManager.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            val soundUri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                notificationManager.getNotificationChannel(CHANNEL_ID_SESSION).sound
            } else {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            }

            val mp = MediaPlayer()
            mp.setDataSource(context, soundUri)
            mp.setWakeMode(context,PowerManager.PARTIAL_WAKE_LOCK)
            mp.setAudioAttributes(
                AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            )
            mp.prepareAsync()
            mp.setOnPreparedListener { player: MediaPlayer ->
                player.start()
            }
            mp.setOnCompletionListener { player: MediaPlayer ->
                player.release()
            }
        }
    }

    fun onBreak(time: String): Notification {

        return NotificationCompat.Builder(context, channelProgressId)
                .setOngoing(true) //прилипает оповещение и можно удалить только програмно
                .setContentTitle(context.getString(R.string.break_time))
                .setContentText(time)
                .setSmallIcon(R.drawable.ic_notif_break)
                .setContentIntent(pendingToMain)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .addAction(0, context.getString(R.string.stop), pendingStop)
                .build()
    }

    fun onBreakEnd() : Notification {

        return NotificationCompat.Builder(context, channelBreakId)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setShowWhen(false)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.dialog_break_end))
                .setContentText(context.getString(R.string.qest_continue))
                .setSmallIcon(R.drawable.ic_notif_break)
                .setContentIntent(pendingToMain)
                .addAction(0, context.getString(R.string.work_start), pendingContinue)
                .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createProgressChannel(): String {
        val channelName = context.getString(R.string.for_progress)
        val channel =
            NotificationChannel(CHANNEL_ID_PROGRESS, channelName, NotificationManager.IMPORTANCE_LOW)
        channel.enableVibration(false)
        notificationManager.createNotificationChannel(channel)
        return CHANNEL_ID_PROGRESS
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createSessionChannel(): String {
        val channelName = context.getString(R.string.for_end_session)
        val channel =
            NotificationChannel(CHANNEL_ID_SESSION, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableVibration(true)
        notificationManager.createNotificationChannel(channel)
        return CHANNEL_ID_SESSION
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createBreakChannel(): String {
        val channelName = context.getString(R.string.for_end_break)
        val channel =
            NotificationChannel(CHANNEL_ID_BREAK, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableVibration(true)
        notificationManager.createNotificationChannel(channel)
        return CHANNEL_ID_BREAK
    }

    companion object {
        const val CHANNEL_ID_SESSION = "chkan.com.channel.session"
        const val CHANNEL_ID_BREAK = "chkan.com.channel.break"
        private val CHANNEL_ID_PROGRESS = "chkan.com.channel.progress"
    }

}