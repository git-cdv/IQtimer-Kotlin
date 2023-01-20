package com.chkan.iqtimer.domain.models


import androidx.lifecycle.MutableLiveData
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.domain.usecases.ProgressUseCase
import com.chkan.iqtimer.utils.ACHIEV_ID_WARRIOR
import com.chkan.iqtimer.utils.SESSIONS
import com.chkan.iqtimer.utils.State
import com.chkan.iqtimer.utils.toTimerFormat
import kotlinx.coroutines.*
import javax.inject.Inject

class Session @Inject constructor (private val pref: PrefManager, private val progressUseCase: ProgressUseCase,private val externalScope: CoroutineScope) {

    val stateLiveData: MutableLiveData<State> = MutableLiveData()
    var timeDefault : String
    var breakDefault : String
    val planLiveData: MutableLiveData<Int> = MutableLiveData()
    val countLiveData: MutableLiveData<Int> = MutableLiveData()
    val timeLiveData: MutableLiveData<String> = MutableLiveData()
    val settingsUpdatedLiveData: MutableLiveData<Pair<Int,String>> = MutableLiveData()

    init {
        stateLiveData.value = State.STOPED
        planLiveData.value = pref.getDefaultPlan()
        countLiveData.value = pref.getCurrentCount()
        timeDefault = pref.getDefaultTime()
        breakDefault = pref.getDefaultBreak()
        timeLiveData.value = timeDefault.toTimerFormat()
        initPremium()
    }

    fun setTimeDefault() {
        timeLiveData.value = timeDefault.toTimerFormat()
    }

    fun cleanCount(){
        countLiveData.postValue(0)
    }

    fun addDoneSession() {
        externalScope.launch {
                val current = pref.getCurrentCount()+1
                pref.addDoneSession(current)
                countLiveData.postValue(current)

                progressUseCase.checkEffectiveCounter(current)
                progressUseCase.checkWeekendAchiev(ACHIEV_ID_WARRIOR)
                progressUseCase.checkGoal(SESSIONS)
            }
    }

    fun addDoneBreak() {
        externalScope.launch {
            progressUseCase.checkBreakAchiev()
        }
    }

    private fun initPremium() {
        externalScope.launch {
            progressUseCase.initPremium()
        }
    }
}