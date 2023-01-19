package com.chkan.iqtimer.domain.usecases

import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.domain.models.Goal
import com.chkan.iqtimer.domain.models.GoalModel
import com.chkan.iqtimer.utils.*
import org.joda.time.LocalDate
import javax.inject.Inject

class ProgressUseCase @Inject constructor(private val pref: PrefManager, private val goal: Goal,private val achievUseCase: AchievementsUseCase) {

    fun checkEffectiveCounter(current:Int) {
        if(pref.getDefaultPlan()==current){//если выполнили план
            val effectiveDateCurrent = LocalDate.parse(pref.getEffectiveDate())
            val today = LocalDate.now()
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
            if (pref.isPremium()){
                achievUseCase.update(ACHIEV_ID_ENTUSIAST)
                checkWeekendAchiev(ACHIEV_ID_HERO)
            }
            checkGoal(EFFECTIVEDAYS)
        }
    }

    fun checkWeekendAchiev(id: Int) {
        val today = LocalDate.now()
        val isWeekend = today.dayOfWeek == 6 || today.dayOfWeek == 7
        if (pref.isPremium() && isWeekend){
            achievUseCase.updateWithDate(id,today.toString())
        }
    }
    fun checkBreakAchiev() {
        achievUseCase.updateWithDate(ACHIEV_ID_STRATEG,LocalDate.now().toString())
    }

    fun checkGoal(type:Int) {
        if(pref.isGoalActive()){
            if(pref.getInt(SP_GOAL_TYPE) == type) {
                val count = pref.getInt(SP_GOAL_CURRENT) + 1
                pref.add(SP_GOAL_CURRENT, count)
                goal.goalCurrent.postValue(count)
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