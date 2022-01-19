package com.chkan.iqtimer.domain.usecases

import android.util.Log
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.data.room.DatabaseModel
import com.chkan.iqtimer.data.room.HistoryDao
import com.chkan.iqtimer.domain.models.Session
import org.joda.time.DateTime
import javax.inject.Inject

class SessionsUseCase @Inject constructor(private val historyDao: HistoryDao, private val prefManager: PrefManager, private val session: Session) {

    fun isFirst(): Boolean{
        return prefManager.isFirst()
    }

    fun startFirst(){
        prefManager.startFirst()
        prefManager.refreshGoal()
    }

    fun checkWorkDate() {
        val workDate = prefManager.getWorkDate()
        if(workDate!=null) {
            val day = DateTime.parse(workDate)
            val today = DateTime.now().toString("yyyy-MM-dd")
            if (workDate != today) {
                    session.cleanCount()
                    historyDao.insert(
                        DatabaseModel(
                            date = workDate,
                            count = prefManager.getCurrentCount(),
                            noMonth = day.monthOfYear,
                            noDayOfWeek = day.dayOfWeek,
                            date_full = DateTime.parse(workDate).toString("E, MMM d, yyyy")
                        )
                    )
                    prefManager.setWorkDateAndCleanSession(today)
            }
        }
    }
}