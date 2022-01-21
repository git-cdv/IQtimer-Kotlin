package com.chkan.iqtimer.domain.usecases

import android.util.Log
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.domain.models.Goal
import com.chkan.iqtimer.domain.models.GoalModel
import com.chkan.iqtimer.utils.*
import org.joda.time.DateTime
import javax.inject.Inject

class ProgressUseCase @Inject constructor(private val pref: PrefManager, private val goal: Goal,private val achievUseCase: AchievementsUseCase) {

    fun getCounter() : Int{
        return pref.getCounter()
    }

    fun checkEffectiveCounter(current:Int) {
        if(pref.getDefaultPlan()==current){//если выполнили план
            val effectiveDateCurrent = DateTime.parse(pref.getEffectiveDate())
            val today = DateTime.now()
            //если эффективный день был вчера
            if(effectiveDateCurrent.dayOfYear()==today.minusDays(1).dayOfYear()){
                val counter = pref.getCounter()+1
                pref.addCounter(counter)
                goal.counter.postValue(counter)
                pref.addEffectiveDate(today)
            } else {
                pref.addCounter(1)
                goal.counter.postValue(1)
                pref.addEffectiveDate(today)
            }
            if (pref.isPremium()){achievUseCase.update(ACHIEV_ID_ENTUSIAST)}
            checkGoal(EFFECTIVEDAYS)
        }
    }

    fun checkGoal(type:Int) {
        if(pref.isGoalActive()){
            if(pref.getInt(SP_GOAL_TYPE) == type) {
                val count = pref.getInt(SP_GOAL_CURRENT) + 1
                pref.add(SP_GOAL_CURRENT, count)
                goal.goalCurrent.postValue(count)
                Log.d("MYAPP", "checkGoal() - count: $count")
                checkGoalDone(count)
            }
        }
    }

    private fun checkGoalDone(count: Int) {
            if(count==pref.getInt(SP_GOAL_PLAN)){
                goal.state.postValue(GOAL_STATUS_DONE)
                pref.add(SP_GOAL_STATUS,GOAL_STATUS_DONE)
                if (pref.isPremium()){achievUseCase.update(ACHIEV_ID_BOSS)}
            }
    }

    fun checkGoalExpired() {
        if (pref.isGoalActive()) {
            val plan = pref.getLong(SP_GOAL_PLAN_TIME)
            if (System.currentTimeMillis() > plan) {
                goal.setExpiredState()
            } else {
                goal.timer.postValue(goal.getRestTime(pref.getLong(SP_GOAL_PLAN_TIME)))
                Log.d("MYAPP", "checkGoalExpired() -timer.postValue")
            }
        }
    }

    fun setNewGoal(new_goal: GoalModel) {
        goal.setNewGoal(new_goal)
    }

    fun isPremium(): Boolean {
         return pref.isPremium()
    }

    fun setPremium(b: Boolean) {
        pref.add(SP_IS_PREMIUM,b)
    }

}