package com.chkan.iqtimer.ui.main

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.chkan.iqtimer.MainActivity
import com.chkan.iqtimer.R
import com.chkan.iqtimer.domain.TimerService
import com.chkan.iqtimer.utils.*
import javax.inject.Inject

class NotifManager @Inject constructor (private val context: Context, private val notificationManager: NotificationManager ) {

    private val channelProgressId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createProgressChannel() else ""
    private val channelResultId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createResultChannel() else ""

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

    fun onEnd(): Notification {

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

        return NotificationCompat.Builder(context, channelResultId)
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
        val channelId = "progress_channel_id"
        val channelName = "For progress"
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        channel.enableVibration(false)
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createResultChannel(): String {
        val channelId = "result_channel_id"
        val channelName = "For result"
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableVibration(true)
        notificationManager.createNotificationChannel(channel)
        return channelId
    }
}