package com.chkan.iqtimer.domain


import androidx.lifecycle.MutableLiveData
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.utils.State
import com.chkan.iqtimer.utils.toTimerFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    fun addDoneSession() {
        GlobalScope.launch (Dispatchers.IO) {
            val current = pref.getCurrentCount()+1
            pref.addDoneSession(current)
            countLiveData.postValue(current)
        }
    }
}