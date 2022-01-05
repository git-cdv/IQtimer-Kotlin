package com.chkan.iqtimer.domain


import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.utils.State
import com.chkan.iqtimer.utils.toTimerFormat
import javax.inject.Inject

class Session @Inject constructor (pref: PrefManager) {

    val stateLiveData: MutableLiveData<State> = MutableLiveData()
    var timeDefault = ObservableField<String>()
    val planLiveData: MutableLiveData<Int> = MutableLiveData()
    val countLiveData: MutableLiveData<Int> = MutableLiveData()
    val timeLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        stateLiveData.value = State.STOPED
        planLiveData.value = pref.getDefaultPlan()
        countLiveData.value = pref.getCurrentCount()
        timeDefault.set(pref.getDefaultTime())
        timeLiveData.value = timeDefault.get()?.toTimerFormat()
    }

    fun setTimeDefault() {
        timeLiveData.value = timeDefault.get()?.toTimerFormat()
    }
}