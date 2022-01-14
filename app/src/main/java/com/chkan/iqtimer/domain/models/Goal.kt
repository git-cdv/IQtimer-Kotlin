package com.chkan.iqtimer.domain.models

import androidx.lifecycle.MutableLiveData
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.utils.*
import javax.inject.Inject

class Goal @Inject constructor (private val pref: PrefManager) {

    val isActive: MutableLiveData<Boolean> = MutableLiveData()
    val type: MutableLiveData<Int> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val desc: MutableLiveData<String> = MutableLiveData()
    val goalCurrent: MutableLiveData<Int> = MutableLiveData()
    val goalPlan: MutableLiveData<Int> = MutableLiveData()
    val daysCurrent: MutableLiveData<Int> = MutableLiveData()
    val daysPlan: MutableLiveData<Int> = MutableLiveData()

    init {
        isActive.value = pref.getBoolean(SP_GOAL_STATUS)
        name.value = pref.getMyString(SP_GOAL_NAME)
        desc.value = pref.getMyString(SP_GOAL_DESC)
        type.value = pref.getInt(SP_GOAL_TYPE)
        goalCurrent.value = pref.getInt(SP_GOAL_CURRENT)
        goalPlan.value = pref.getInt(SP_GOAL_PLAN)
        daysCurrent.value = pref.getInt(SP_GOAL_DAYS_CURRENT)
        daysPlan.value = pref.getInt(SP_GOAL_DAYS_PLAN)
    }

    fun setNewGoal(goal: GoalModel){
        isActive.value = true
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
        isActive.value = false
        type.value = 0
        name.value = nameDef
        desc.value = descDef
        goalCurrent.value = 0
        goalPlan.value = 0
        daysCurrent.value = 0
        daysPlan.value = 0
        pref.refreshGoal()
    }
}