package com.chkan.iqtimer.domain.usecases

import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.data.room.DatabaseModel
import com.chkan.iqtimer.data.room.HistoryDao
import org.joda.time.DateTime
import javax.inject.Inject

class SessionsUseCase @Inject constructor(private val historyDao: HistoryDao, private val prefManager: PrefManager) {

    fun isFirst(): Boolean{
        return prefManager.isFirst()
    }

    fun startFirst(){
        prefManager.startFirst()
        /*var day = DateTime.now()
        for(item in 1..30){
            historyDao.insert(DatabaseModel(date = day.toString("yyyy-MM-dd"), count = 1, noMonth = day.monthOfYear , noDayOfWeek = day.dayOfWeek,date_full = day.toString("E, MMM d, yyyy")))
            day = day.minusDays(1)
        }*/
    }

    fun checkWorkDate() {
        val workDate = prefManager.getWorkDate()
        val day = DateTime.parse(workDate)
        val today = DateTime.now().toString("yyyy-MM-dd")
        if(workDate!=today){
            if(!workDate.isNullOrEmpty()){
                historyDao.insert(DatabaseModel(date = workDate, count = prefManager.getCurrentCount(), noMonth = day.monthOfYear , noDayOfWeek = day.dayOfWeek,date_full = DateTime.parse(workDate).toString("E, MMM d, yyyy")))
                prefManager.addDoneSession(0)
                prefManager.setWorkDate(today)
            }
        }
    }
}