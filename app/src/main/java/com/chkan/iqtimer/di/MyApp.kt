package com.chkan.iqtimer.di

import android.app.Application
import com.chkan.iqtimer.data.PrefManager
import dagger.hilt.android.HiltAndroidApp
import org.joda.time.DateTime
import javax.inject.Inject
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.chkan.iqtimer.domain.workers.StartWorker
import com.chkan.iqtimer.utils.SP_IS_PERIODIC_STARTED
import java.util.concurrent.TimeUnit


@HiltAndroidApp
class MyApp: Application(){

    @Inject
    lateinit var prefManager: PrefManager

    override fun onCreate() {
        super.onCreate()
        startPeriodicWork()
    }

    private fun startPeriodicWork() {

        if(!prefManager.isPeriodicStarted()){
            //расчитываю минуты до 24.00 (-15 минут)
            val minutesTo24 = 1425 - DateTime.now().minuteOfDay().get()
            val startWorkRequest = OneTimeWorkRequest.Builder(StartWorker::class.java)
                .setInitialDelay(minutesTo24.toLong(), TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(this).enqueue(startWorkRequest)
            prefManager.add(SP_IS_PERIODIC_STARTED,true)
        }
    }
}