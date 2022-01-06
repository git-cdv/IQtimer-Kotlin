package com.chkan.iqtimer.domain

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.chkan.iqtimer.MainActivity
import com.chkan.iqtimer.R
import com.chkan.iqtimer.utils.*
import javax.inject.Inject

class NotifManager @Inject constructor (private val context: Context) {

    val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel() else ""
    val intentMain = Intent(context, MainActivity::class.java)
    val pendingToMain = PendingIntent.getActivity(context, 1, intentMain, 0)
    val stopIntent = Intent(context, TimerService::class.java).putExtra(KEY_COMMAND, COM_STOP_NOTIF)
    val pendingStop = PendingIntent.getService(context, 2, stopIntent, 0)
    val pauseIntent = Intent(context, TimerService::class.java).putExtra(KEY_COMMAND, COM_PAUSE_NOTIF)
    val pendingPause = PendingIntent.getService(context, 3, pauseIntent, 0)
    val continueIntent = Intent(context, TimerService::class.java).putExtra(KEY_COMMAND, COM_RUN_NOTIF)
    val pendingContinue = PendingIntent.getService(context, 6, continueIntent, 0)
    val startBreakIntent = Intent(context, TimerService::class.java).putExtra(KEY_COMMAND, COM_BREAK_NOTIF)
    val pendingBreak = PendingIntent.getService(context, 8, startBreakIntent, 0)

    fun onRun(time: String): Notification {

        return NotificationCompat.Builder(context, channelId)
            .setOngoing(true) //прилипает оповещение и можно удалить только програмно
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentTitle(context.getString(R.string.dowork))
            .setContentText(time)
            .setSmallIcon(R.drawable.ic_notif_timer)
            .setContentIntent(pendingToMain)
            .addAction(0, context.getString(R.string.stop), pendingStop)
            .addAction(0, context.getString(R.string.pause), pendingPause)
            .build()
    }

    fun onPause(): Notification {

        return NotificationCompat.Builder(context, channelId)
                .setOngoing(false) //прилипает оповещение и можно удалить только програмно
                .setContentTitle(context.getString(R.string.on_pause))
                .setContentText(context.getString(R.string.qest_continue))
                .setSmallIcon(R.drawable.ic_notif_pause)
                .setContentIntent(pendingToMain)
                .addAction(0, context.getString(R.string.stop), pendingStop)
                .addAction(0, context.getString(R.string.dialog_continue), pendingContinue)
                .build()
    }

    fun onEnd(): Notification {

        return NotificationCompat.Builder(context, channelId)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
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

        return NotificationCompat.Builder(context, channelId)
                .setOngoing(true) //прилипает оповещение и можно удалить только програмно
                .setContentTitle(context.getString(R.string.break_time))
                .setContentText(time)
                .setSmallIcon(R.drawable.ic_notif_break)
                .setContentIntent(pendingToMain)
                .addAction(0, context.getString(R.string.stop), pendingStop)
                .build()
    }

    fun onBreakEnd() : Notification {

        return NotificationCompat.Builder(context, channelId)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
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
    private fun createChannel(): String {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "my_service_channelid"
        //название которое видит пользователь в настройках
        val channelName = "IQtimer Notification"
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.importance = NotificationManager.IMPORTANCE_NONE
        notificationManager.createNotificationChannel(channel)
        return channelId
    }
}