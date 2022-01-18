package com.chkan.iqtimer.domain.usecases

import android.util.Log
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.domain.models.Goal
import com.chkan.iqtimer.utils.*
import org.joda.time.DateTime
import javax.inject.Inject

class ProgressUseCase @Inject constructor(private val pref: PrefManager, private val goal: Goal) {

    fun getCounter() : Int{
        return pref.getCounter()
    }

    fun checkEffectiveCounter(current:Int) {
        if(pref.getDefaultPlan()==current){//если выполнили план
            val effectiveDateCurrent = DateTime.parse(pref.getEffectiveDate())
            val today = DateTime.now()
            //если эффективный день был вчера
            if(effectiveDateCurrent.dayOfYear()==today.minusDays(1).dayOfYear()){
                pref.addCounter(pref.getCounter()+1)
                pref.addEffectiveDate(today)
            } else {
                pref.addCounter(1)
                pref.addEffectiveDate(today)
            }
            checkGoal(EFFECTIVEDAYS)
        }
    }

    fun checkGoal(type:Int) {
        if(pref.isGoalActive() && pref.getInt(SP_GOAL_TYPE) == type){
            val count = pref.getInt(SP_GOAL_CURRENT)+1
            pref.add(SP_GOAL_CURRENT,count)
            goal.goalCurrent.postValue(count)
        }
    }

}