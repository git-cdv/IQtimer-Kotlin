package com.chkan.iqtimer.domain.models


import androidx.lifecycle.MutableLiveData
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.utils.State
import com.chkan.iqtimer.utils.toTimerFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

class Session @Inject constructor (private val pref: PrefManager) {

    val stateLiveData: MutableLiveData<State> = MutableLiveData()
    var timeDefault : String
    var breakDefault : String
    val planLiveData: MutableLiveData<Int> = MutableLiveData()
    val countLiveData: MutableLiveData<Int> = MutableLiveData()
    val timeLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        stateLiveData.value = State.STOPED
        planLiveData.value = pref.getDefaultPlan()
        countLiveData.value = pref.getCurrentCount()
        timeDefault = pref.getDefaultTime()
        breakDefault = pref.getDefaultBreak()
        timeLiveData.value = timeDefault.toTimerFormat()
    }

    fun setTimeDefault() {
        timeLiveData.value = timeDefault.toTimerFormat()
    }

    fun cleanCount(){
        countLiveData.value =0
    }

    fun addDoneSession() {
        GlobalScope.launch (Dispatchers.IO) {
            val current = pref.getCurrentCount()+1
            pref.addDoneSession(current)
            countLiveData.postValue(current)

            if(pref.getDefaultPlan()==current){//если выполнили план
                val effectiveDateCurrent = DateTime.parse(pref.getEffectiveDate())
                val today = DateTime.now()
                //если эффективный день был вчера
                if(effectiveDateCurrent.dayOfYear()==today.minusDays(1).dayOfYear()){
                    pref.addCounter(pref.getCounter()+1)
                    pref.addEffectiveDate(today)
                } else {
                    pref.addCounter(1)
                    pref.addEffectiveDate(today)
                }

            }
        }
    }
}