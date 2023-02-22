package com.chkan.iqtimer.domain.usecases

import android.util.Log
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.data.room.DatabaseModel
import com.chkan.iqtimer.data.room.HistoryDao
import com.chkan.iqtimer.domain.models.ChartModel
import com.github.mikephil.charting.data.BarEntry
import org.joda.time.DateTime
import java.util.ArrayList
import javax.inject.Inject

class StatisticUseCase @Inject constructor(private val historyDao: HistoryDao, private val prefManager: PrefManager)  {

    private val listTotal by lazy { historyDao.getTotal() }

    fun getCountToday() : Int {
       return prefManager.getCurrentCount()
    }

    fun getListFull() : List<DatabaseModel> {
        return if (listTotal.isNullOrEmpty()) {
            listOf(DatabaseModel(count = prefManager.getCurrentCount(), date_full = DateTime.now().toString("E, MMM d, yyyy"), date = "", noMonth = 0, noDayOfWeek = 0 ))
        } else {
            val list = listTotal.toMutableList()
            list.add(DatabaseModel(count = prefManager.getCurrentCount(), date_full = DateTime.now().toString("E, MMM d, yyyy"), date = "", noMonth = 0, noDayOfWeek = 0 ))
            list.reversed()
        }
    }

    fun getCountWeek(): Int {
        var count =0
        val list = historyDao.getWeek()
        var check = list.firstOrNull()?.noDayOfWeek
        if(check!=7) {//если вчера было ВС - то это новая неделя
            for (raw in list) {
                count += raw.count
                if (check != null) {
                    check -= 1
                    if (check == 0) {
                        break
                    }
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
        var mIndex = 0F

        if (listTotal.isNullOrEmpty()) { //если 0-вой вход
            //Добавляем сегоднешнюю дату
            titles.add(DateTime.now().toString("MMMdd"))
            //Добавляем индекс и счетчик в массив
            data.add(BarEntry(0F, prefManager.getCurrentCount().toFloat()))
        } else {
            for ((index, item) in listTotal.withIndex()) {
                data.add(BarEntry(index.toFloat(),item.count.toFloat()))
                val date = DateTime.parse(item.date).toString("MMMdd")
                titles.add(date)
                mIndex=index.toFloat()
            }
            //Добавляем сегоднешнюю дату
            titles.add(DateTime.now().toString("MMMdd"))
            data.add(BarEntry(mIndex+1, prefManager.getCurrentCount().toFloat()))
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
            data.add(BarEntry(0F, prefManager.getCurrentCount().toFloat()))
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

                if (index==listTotal.lastIndex) {
                        data.add(BarEntry(mIndex, mCountMonth+prefManager.getCurrentCount()))
                        titles.add(currentDate.toString("MMM"))
                }
            }
        }

        return ChartModel(data,titles.toTypedArray(),0)
    }
}