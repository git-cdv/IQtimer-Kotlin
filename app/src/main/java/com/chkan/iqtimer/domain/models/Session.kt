package com.chkan.iqtimer.domain.models


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.domain.usecases.ProgressUseCase
import com.chkan.iqtimer.utils.SESSIONS
import com.chkan.iqtimer.utils.State
import com.chkan.iqtimer.utils.toTimerFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class Session @Inject constructor (private val pref: PrefManager, private val progressUseCase: ProgressUseCase) {

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

                progressUseCase.checkEffectiveCounter(current)
                progressUseCase.checkGoal(SESSIONS)
            }
    }
}