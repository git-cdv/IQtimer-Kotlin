package com.chkan.iqtimer.domain.models

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.chkan.iqtimer.R
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.utils.*
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

class Goal @Inject constructor (private val pref: PrefManager) {

    val state: MutableLiveData<Int> = MutableLiveData()
    val type: MutableLiveData<Int> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val desc: MutableLiveData<String> = MutableLiveData()
    val goalCurrent: MutableLiveData<Int> = MutableLiveData()
    val goalPlan: MutableLiveData<Int> = MutableLiveData()
    val timer: MutableLiveData<String> = MutableLiveData()
    val counter: MutableLiveData<Int> = MutableLiveData()

    init {
        state.value = pref.getInt(SP_GOAL_STATUS)
        name.value = pref.getGoalName()
        desc.value = pref.getGoalDesc()
        type.value = pref.getInt(SP_GOAL_TYPE)
        goalCurrent.value = pref.getInt(SP_GOAL_CURRENT)
        goalPlan.value = pref.getInt(SP_GOAL_PLAN)
        timer.value = if (pref.getLong(SP_GOAL_PLAN_TIME)==0L) "0" else getRestTime(pref.getLong(SP_GOAL_PLAN_TIME))
        counter.value = pref.getCounter()
    }

    fun setNewGoal(goal: GoalModel){
        state.value = GOAL_STATUS_ACTIVE
        type.value = goal.type
        name.value = goal.name
        desc.value = goal.desc
        goalCurrent.value = 0
        goalPlan.value = goal.plan
        //86400000 - кол-во миллисекунд в дне
        val timeLong = System.currentTimeMillis() + (goal.days_plan.toLong()*86400000)
        timer.value = setPlanTime(timeLong)
        pref.setNewGoal(goal.name,goal.desc,goal.plan,timeLong,goal.type)
    }

    fun deleteGoal(nameDef: String, descDef: String) {
        state.value = GOAL_STATUS_INACTIVE
        type.value = 0
        name.value = nameDef
        desc.value = descDef
        goalCurrent.value = 0
        goalPlan.value = 0
        timer.value = "0"
        pref.refreshGoal()
    }

    fun getExpiredText(text:String): String {
        val current = pref.getInt(SP_GOAL_CURRENT)
        val plan = pref.getInt(SP_GOAL_PLAN)
        val percent = current.toDouble() / plan.toDouble() * 100
        val result = percent.roundToInt()
        return "$text $result %."
    }

    private fun setPlanTime(timeLong: Long): String {
        pref.add(SP_GOAL_PLAN_TIME,timeLong)
        return getRestTime(timeLong)
    }

    fun getRestTime(timeLong:Long): String {
        val diff = timeLong - System.currentTimeMillis()
        return if(diff<86400000){
            val hours = diff / 3600000
            val minutes = (diff % 3600000)/60000
            val h = pref.getShort('h')
            val m = pref.getShort('m')
            "$hours $h $minutes $m"
        } else {
            val days = diff / 86400000
            val hours = (diff % 86400000) / 3600000
            val h = pref.getShort('h')
            val d = pref.getShort('d')
            "$days $d $hours $h"
        }
    }

    fun getRestTimeLong(): Long {
        return pref.getLong(SP_GOAL_PLAN_TIME) - System.currentTimeMillis()
    }


    fun isDayLeft(): Boolean {
        val plan = pref.getLong(SP_GOAL_PLAN_TIME)
        val diff = plan - System.currentTimeMillis()
        return diff<86400000
    }

    fun setExpiredState(){
        state.postValue(GOAL_STATUS_EXPIRED)
        pref.add(SP_GOAL_STATUS, GOAL_STATUS_EXPIRED)
    }

}