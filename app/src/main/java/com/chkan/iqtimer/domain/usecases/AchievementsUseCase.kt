package com.chkan.iqtimer.domain.usecases

import android.content.Context
import android.util.Log
import com.chkan.iqtimer.R
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.data.room.AchievDao
import com.chkan.iqtimer.data.room.Achievements
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AchievementsUseCase @Inject constructor(private val ctx: Context, private val achievDao: AchievDao, private val pref: PrefManager){

    private val typePlans = arrayListOf(arrayOf(5,10,30,50,75,100,150,200,275,365), arrayOf(2,6,10,14,20,30,40,50,75,100),
        arrayOf(3,3,3,3,3,3,3,3,3,3,3), arrayOf(6,6,6,6,6,6,6,6,6,6,6))

    val achievementsFlow: Flow<List<Achievements>>
        get() = achievDao.getAchievementsFlow()

    fun initAchievements(){
        val list = mutableListOf<Achievements>()
        list.add(Achievements(0,ctx.resources.getString(R.string.p_title_entuziast),0,0,5,0))
        list.add(Achievements(1,ctx.resources.getString(R.string.p_title_voin),0,0,2,1))
        list.add(Achievements(2,ctx.resources.getString(R.string.p_title_boss),0,0,2,1))
        list.add(Achievements(3,ctx.resources.getString(R.string.p_title_pokoritel),0,0,5,0))
        list.add(Achievements(4,ctx.resources.getString(R.string.p_title_hero),0,0,2,1))
        list.add(Achievements(5,ctx.resources.getString(R.string.p_title_legenda),0,0,2,2))
        list.add(Achievements(6,ctx.resources.getString(R.string.p_title_pobeditel),0,0,6,3))
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
        } else{
            achiev.current = count
            achievDao.update(achiev)
        }
    }
}