package com.chkan.iqtimer.domain

import com.chkan.iqtimer.data.PrefManager
import javax.inject.Inject

class Session @Inject constructor (pref: PrefManager) {

    enum class State {START,STOP,PAUSE}

    var state: State = State.STOP
    var defPlan: Int = pref.getDefaultPlan()
    var timer: Int = pref.getDefaultTime()
    var currentCount: Int = pref.getCurrentCount()

}