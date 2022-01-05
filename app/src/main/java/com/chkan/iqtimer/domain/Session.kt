package com.chkan.iqtimer.domain


import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.chkan.iqtimer.data.PrefManager
import javax.inject.Inject

class Session @Inject constructor (pref: PrefManager) {

    enum class State {ACTIVE,STOPED,PAUSED,BREAK}

    val stateLiveData: MutableLiveData<State> = MutableLiveData()
    var timeDefault = ObservableField<String>()
    val planLiveData: MutableLiveData<Int> = MutableLiveData()
    val countLiveData: MutableLiveData<Int> = MutableLiveData()

    init {
        stateLiveData.value = State.STOPED
        planLiveData.value = pref.getDefaultPlan()
        countLiveData.value = pref.getCurrentCount()
        timeDefault.set(pref.getDefaultTime())
    }
}