package com.chkan.iqtimer.domain.usecases

import android.content.Context
import com.chkan.iqtimer.R
import com.chkan.iqtimer.data.room.AchievDao
import com.chkan.iqtimer.data.room.Achievements
import com.chkan.iqtimer.utils.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AchievementsUseCase @Inject constructor(private val ctx: Context, private val achievDao: AchievDao){

    private val typePlans = arrayListOf(arrayOf(5,7,10,14,28,35,70,100,120,180), arrayOf(2,6,10,14,20,30,40,50,60,75))

    val achievementsFlow: Flow<List<Achievements>>
        get() = achievDao.getAchievementsFlow()

    fun initAchievements(){
        val list = mutableListOf<Achievements>()
        list.add(Achievements(0,ctx.resources.getString(R.string.p_title_entuziast),0,0,5,0,""))
        list.add(Achievements(1,ctx.resources.getString(R.string.p_title_voin),0,0,2,1,""))
        list.add(Achievements(2,ctx.resources.getString(R.string.p_title_boss),0,0,2,1,""))
        list.add(Achievements(3,ctx.resources.getString(R.string.p_title_strateg),0,0,5,0,""))
        list.add(Achievements(4,ctx.resources.getString(R.string.p_title_hero),0,0,2,1,""))
        list.add(Achievements(5,ctx.resources.getString(R.string.p_title_legenda),0,0,3,-1,""))
        list.add(Achievements(6,ctx.resources.getString(R.string.p_title_pobeditel),0,0,6,-1,""))
        achievDao.insertList(list)
    }

    fun update(id:Int) {
        val achiev = achievDao.getAchievementForId(id)
        val count = achiev.current+1
        if(count==achiev.plan){
            val level = achiev.level+1
            achiev.level = level
            achiev.current = count
            achiev.plan = typePlans[achiev.planIndex][level.coerceAtMost(9)]
            achievDao.update(achiev)
            if (id==ACHIEV_ID_ENTUSIAST) checkLegendAchiev(achiev)
            if (level==10) upWinnerAchiev()
        } else{
            achiev.current = count
            achievDao.update(achiev)
        }
    }

    fun updateWithDate(id: Int, today: String) {
        val achiev = achievDao.getAchievementForId(id)
        if(achiev.lastResultDay != today){
            val count = achiev.current+1

            if(count==achiev.plan){
                val level = achiev.level+1
                achiev.level = level
                achiev.current = count
                achiev.plan = typePlans[achiev.planIndex][level.coerceAtMost(9)]
                achiev.lastResultDay = today
                achievDao.update(achiev)
                if (id==ACHIEV_ID_HERO || id==ACHIEV_ID_STRATEG) checkLegendAchiev(achiev)
                if (level==10) upWinnerAchiev()
            } else{
                achiev.current = count
                achiev.lastResultDay = today
                achievDao.update(achiev)
            }
        }
    }

    private fun checkLegendAchiev(achiev: Achievements) {
        val legendAchiv = achievDao.getAchievementForId(ACHIEV_ID_LEGEND)
        val legendPlan = legendAchiv.level +1
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
            if (count==3) {
                val level = legendAchiv.level+1
                legendAchiv.level = level
                legendAchiv.current = countNext
                achievDao.update(legendAchiv)
                if (level==10) upWinnerAchiev()
            } else {
                legendAchiv.current = count
                achievDao.update(legendAchiv)
            }
        }
    }

    private fun upWinnerAchiev() {
        val achiev = achievDao.getAchievementForId(ACHIEV_ID_WINNER)
        achiev.current = achiev.current+1
        achievDao.update(achiev)
    }
}