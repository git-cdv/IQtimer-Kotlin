package com.chkan.iqtimer.domain.workers

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit


class StartWorker (ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {

        try {
            val periodicWorkRequest = PeriodicWorkRequest.Builder(
                PeriodicWorker::class.java,
                24,
                TimeUnit.HOURS,
                23,
                TimeUnit.HOURS)
                .build()

            WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
            val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            pref.edit().putString("StartWorker_SUCCES", "Date ${DateTime.now()}").apply()
            return Result.success()

        } catch (e: Exception){
            val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            pref.edit().putString("EX_from_StartWorker", "Exception: ${e.message}, date: ${DateTime.now()}").apply()
            return Result.failure()
        }
    }
}