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

        if (listTotal.isNullOrEmpty()) { //если 0-вой вход
            //Добавляем сегоднешнюю дату
            titles.add(DateTime.now().toString("MMMdd"))
            //Добавляем индекс и счетчик в массив
            data.add(BarEntry(0F, currentCount.toFloat()))
        } else {
            for ((index, item) in listTotal.withIndex()) {
                data.add(BarEntry(index.toFloat(),item.count.toFloat()))
                val date = DateTime.parse(item.date).toString("MMMdd")
                titles.add(date)
            }
            /*
            * Подумать как добавить результаты за сегодня
            * */
        }

        return ChartModel(data,titles.toTypedArray(),prefManager.getDefaultPlan())
    }

    fun getDataMonth(): ChartModel {
        val data: ArrayList<BarEntry> = arrayListOf()
        val titles: ArrayList<String> = arrayListOf()
        var mCheck = 0
        var mIndex = 0F
        var mCountMonth = 0F
        var mCurrentMonth: Int
        var mMonth = 0

        if (listTotal.isNullOrEmpty()) { //если 0-вой вход
            //Добавляем сегоднешнюю дату
            titles.add(DateTime.now().toString("MMM"))
            //Добавляем индекс и счетчик в массив
            data.add(BarEntry(0F, currentCount.toFloat()))
        } else {

            for ((index, item) in listTotal.withIndex()) {

                //получаем текущую дату
                val currentDate = DateTime.parse(item.date)

                if (mCheck == 0) { //проверяем что это первый проход
                    mMonth = currentDate.monthOfYear
                    mCheck = 1
                }

                mCurrentMonth = currentDate.monthOfYear

                if (mMonth == mCurrentMonth) {
                    mCountMonth += item.count
                } else {
                    //Добавляем индекс и счетчик в массив
                    data.add(BarEntry(mIndex, mCountMonth))
                    //Добавляем месяц в формате fmtOut в массив для замен на XAxis
                    titles.add(DateTime.now().withMonthOfYear(mMonth).toString("MMM"))
                    mCheck = 0
                    mIndex++
                    mCountMonth = item.count.toFloat()
                }

                /*
                Подумать как добавить результаты за сегодня

                if (index==listTotal.lastIndex) {
                      //Добавляем индекс и счетчик в массив
                      data.add(BarEntry(mIndex-1, mCountMonth+currentCount))
                      titles.add(currentDate.minusMonths(1).toString("MMM"))
                  }*/
            }
        }

        return ChartModel(data,titles.toTypedArray(),0)
    }
}