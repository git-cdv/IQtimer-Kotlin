package com.chkan.iqtimer.domain.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.joda.time.DateTime
import javax.inject.Inject


class PeriodicWorker @Inject constructor (ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        return try {
            //historyDao.insert(DatabaseModel(date = DateTime.now().toString(),count=10, date_full = DateTime.now().dayOfMonth().toString() ))
            val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            pref.edit().putString("PeriodicWorker_SUCCES", "Date ${DateTime.now()}").apply()
            Result.success()
        } catch (e: Exception){
            val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            pref.edit().putString("EX_from_PeriodicWorker", "Exception: ${e.message}, date: ${DateTime.now()}").apply()
            Result.failure()
        }
    }
}