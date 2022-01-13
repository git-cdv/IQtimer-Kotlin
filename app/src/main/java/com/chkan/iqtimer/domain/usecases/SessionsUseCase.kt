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
    /*    val yes = DateTime.now().minusDays(1)
        var day = DateTime.parse("2021-12-15")

        for(item in 1..35){
            day = day.plusDays(1)
            if(day.dayOfYear()!=yes.dayOfYear()) {
                historyDao.insert(
                    DatabaseModel(
                        date = day.toString("yyyy-MM-dd"),
                        count = 1,
                        noMonth = day.monthOfYear,
                        noDayOfWeek = day.dayOfWeek,
                        date_full = day.toString("E, MMM d, yyyy")
                    )
                )
            }
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