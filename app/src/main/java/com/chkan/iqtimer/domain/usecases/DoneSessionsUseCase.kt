package com.chkan.iqtimer.domain.usecases

import com.chkan.iqtimer.data.room.DatabaseModel
import com.chkan.iqtimer.data.room.HistoryDao
import org.joda.time.DateTime
import javax.inject.Inject

class DoneSessionsUseCase @Inject constructor(private val historyDao: HistoryDao) {

    fun writeDoneSession(count: Int, workDate:String){
        historyDao.insert(DatabaseModel(date = workDate, count = count, date_full = DateTime.parse(workDate).toString("E, MMM d, yyyy")))
    }
}