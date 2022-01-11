package com.chkan.iqtimer.domain.usecases

import android.util.Log
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.data.room.HistoryDao
import com.chkan.iqtimer.domain.models.ChartModel
import com.github.mikephil.charting.data.BarEntry
import org.joda.time.DateTime
import java.util.ArrayList
import javax.inject.Inject

class StatisticUseCase @Inject constructor(private val historyDao: HistoryDao, private val prefManager: PrefManager)  {

    private val listTotal by lazy { historyDao.getTotal() }
    private val currentCount by lazy { prefManager.getCurrentCount() }

    fun getCountToday() : Int {
       return currentCount
    }

    fun getCountWeek(): Int {
        var count =0
        val list = historyDao.getWeek()
        var check = list.firstOrNull()?.noDayOfWeek
        for (raw in list){
            count += raw.count
            if (check != null) {
                check -= 1
                if(check==0){
                    break
                }
            }
        }
        return count
    }

    fun getCountMonth(): Int {
        var count =0
        val list = historyDao.getMonth()
        val check = list.firstOrNull()?.noMonth
        for (raw in list){
            if (check != raw.noMonth) {
                    break
                } else {
                count += raw.count
                }
            }
        return count
    }

    fun getCountTotal(): Int {
        var count =0
        for (raw in listTotal){
            count += raw.count
        }
        return count
    }

    fun getDataDays(): ChartModel {
        val data: ArrayList<BarEntry> = arrayListOf()
        val titles: ArrayList<String> = arrayListOf()

        for ((index, item) in listTotal.withIndex()) {
            data.add(BarEntry(index.toFloat(),item.count.toFloat()))
            val date = DateTime.parse(item.date).toString("MMMdd")
            titles.add(date)
        }
        return ChartModel(data,titles.toTypedArray(),currentCount)
    }
}