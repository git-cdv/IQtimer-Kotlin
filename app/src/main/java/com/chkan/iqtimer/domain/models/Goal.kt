package com.chkan.iqtimer.domain.models

import androidx.lifecycle.MutableLiveData
import com.chkan.iqtimer.R
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.utils.*
import javax.inject.Inject
import kotlin.math.roundToInt

class Goal @Inject constructor (private val pref: PrefManager) {

    val state: MutableLiveData<Int> = MutableLiveData()
    val type: MutableLiveData<Int> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val desc: MutableLiveData<String> = MutableLiveData()
    val goalCurrent: MutableLiveData<Int> = MutableLiveData()
    val goalPlan: MutableLiveData<Int> = MutableLiveData()
    val daysCurrent: MutableLiveData<Int> = MutableLiveData()
    val daysPlan: MutableLiveData<Int> = MutableLiveData()
    val counter: MutableLiveData<Int> = MutableLiveData()

    init {
        state.value = pref.getInt(SP_GOAL_STATUS)
        name.value = pref.getGoalName()
        desc.value = pref.getGoalDesc()
        type.value = pref.getInt(SP_GOAL_TYPE)
        goalCurrent.value = pref.getInt(SP_GOAL_CURRENT)
        goalPlan.value = pref.getInt(SP_GOAL_PLAN)
        daysCurrent.value = pref.getInt(SP_GOAL_DAYS_CURRENT)
        daysPlan.value = pref.getInt(SP_GOAL_DAYS_PLAN)
        counter.value = pref.getCounter()
    }

    fun setNewGoal(goal: GoalModel){
        state.value = GOAL_STATUS_ACTIVE
        type.value = goal.type
        name.value = goal.name
        desc.value = goal.desc
        goalCurrent.value = 0
        goalPlan.value = goal.plan
        daysCurrent.value = 0
        daysPlan.value = goal.days_plan

        pref.setNewGoal(goal.name,goal.desc,goal.plan,goal.days_plan,goal.type)
    }

    fun deleteGoal(nameDef: String, descDef: String) {
        state.value = GOAL_STATUS_INACTIVE
        type.value = 0
        name.value = nameDef
        desc.value = descDef
        goalCurrent.value = 0
        goalPlan.value = 0
        daysCurrent.value = 0
        daysPlan.value = 0
        pref.refreshGoal()
    }

    fun getExpiredText(text:String): String {
        val current = pref.getInt(SP_GOAL_CURRENT)
        val plan = pref.getInt(SP_GOAL_PLAN)
        val percent = current.toDouble() / plan.toDouble() * 100
        val result = percent.roundToInt()
        return "$text $result %."
    }
}