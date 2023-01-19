package com.chkan.iqtimer.domain.usecases

import android.content.Context
import com.chkan.iqtimer.R
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.data.room.AchievDao
import com.chkan.iqtimer.data.room.Achievements
import com.chkan.iqtimer.utils.ACHIEV_ID_ENTUSIAST
import com.chkan.iqtimer.utils.ACHIEV_ID_HERO
import com.chkan.iqtimer.utils.ACHIEV_ID_LEGEND
import com.chkan.iqtimer.utils.ACHIEV_ID_STRATEG
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AchievementsUseCase @Inject constructor(private val ctx: Context, private val achievDao: AchievDao, private val pref: PrefManager){

    private val typePlans = arrayListOf(arrayOf(5,7,10,14,28,35,70,100,120,180), arrayOf(2,6,10,14,20,30,40,50,60,75),
        arrayOf(3,3,3,3,3,3,3,3,3,3,3), arrayOf(6,6,6,6,6,6,6,6,6,6,6))

    val achievementsFlow: Flow<List<Achievements>>
        get() = achievDao.getAchievementsFlow()

    fun initAchievements(){
        val list = mutableListOf<Achievements>()
        list.add(Achievements(0,ctx.resources.getString(R.string.p_title_entuziast),0,0,5,0,""))
        list.add(Achievements(1,ctx.resources.getString(R.string.p_title_voin),0,0,2,1,""))
        list.add(Achievements(2,ctx.resources.getString(R.string.p_title_boss),0,0,2,1,""))
        list.add(Achievements(3,ctx.resources.getString(R.string.p_title_strateg),0,0,5,0,""))
        list.add(Achievements(4,ctx.resources.getString(R.string.p_title_hero),0,0,2,1,""))
        list.add(Achievements(5,ctx.resources.getString(R.string.p_title_legenda),0,0,3,2,""))
        list.add(Achievements(6,ctx.resources.getString(R.string.p_title_pobeditel),0,0,6,3,""))
        achievDao.insertList(list)
    }

    fun update(id:Int) {
        val achiev = achievDao.getAchievementForId(id)
        val count = achiev.current+1

        if(count==achiev.plan){
            val level = achiev.level+1
            achiev.level = level
            achiev.current = count
            achiev.plan = typePlans[achiev.planIndex][level]
            achievDao.update(achiev)
            if (id==ACHIEV_ID_ENTUSIAST || id==ACHIEV_ID_STRATEG) checkLegendAchiev(achiev)
        } else{
            achiev.current = count
            achievDao.update(achiev)
        }
    }

    private fun checkLegendAchiev(achiev: Achievements) {
        val legendAchiv = achievDao.getAchievementForId(ACHIEV_ID_LEGEND)
        val legendPlan = legendAchiv.level +1
        //первое число по текущему плану, второе - выполненных на след уровне
        val count = legendCount(legendPlan,achiev)
        if (count.first==3) {
            val level = legendAchiv.level+1
            legendAchiv.level = level
            legendAchiv.current = count.second
            achievDao.update(legendAchiv)
        } else {
            legendAchiv.current = count.first
            achievDao.update(legendAchiv)
        }
    }

    private fun legendCount(legendPlan: Int, achiev: Achievements) : Pair<Int,Int> {
        val listIds = arrayListOf(ACHIEV_ID_ENTUSIAST, ACHIEV_ID_HERO, ACHIEV_ID_STRATEG)
        var count = 0
        var countNext = 0
        if (achiev.level == legendPlan) {
            count++
            listIds.remove(achiev.id)
            listIds.onEach {
                val achievLevel = achievDao.getAchievementForId(it).level
                if (achievLevel >= legendPlan) count++
                if (achievLevel >= legendPlan+1) countNext++
            }
        }
        return Pair(count,countNext)
    }

    fun updateWithDate(id: Int, today: String) {
        val achiev = achievDao.getAchievementForId(id)
        if(achiev.lastResultDay != today){
            val count = achiev.current+1

            if(count==achiev.plan){
                val level = achiev.level+1
                achiev.level = level
                achiev.current = count
                achiev.plan = typePlans[achiev.planIndex][level]
                achiev.lastResultDay = today
                achievDao.update(achiev)
            } else{
                achiev.current = count
                achiev.lastResultDay = today
                achievDao.update(achiev)
            }
        }

    }
}