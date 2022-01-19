package com.chkan.iqtimer.domain.usecases

import android.content.Context
import com.chkan.iqtimer.R
import com.chkan.iqtimer.data.room.AchievDao
import com.chkan.iqtimer.data.room.Achievements
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AchievementsUseCase @Inject constructor(private val ctx: Context, private val achievDao: AchievDao){

    val achievementsFlow: Flow<List<Achievements>>
        get() = achievDao.getAchievementsFlow()

    fun initAchievements(){
        val list = mutableListOf<Achievements>()
        list.add(Achievements(0,ctx.resources.getString(R.string.p_title_entuziast),0,0,5,"5,10,30,50,75,100,150,200,250,300,365"))
        list.add(Achievements(1,ctx.resources.getString(R.string.p_title_voin),0,0,2,"2,6,10,14,20,30,40,50,60,80,100"))
        list.add(Achievements(2,ctx.resources.getString(R.string.p_title_boss),0,0,2,"2,6,10,14,20,30,40,50,60,80,100"))
        list.add(Achievements(3,ctx.resources.getString(R.string.p_title_pokoritel),0,0,3,"3,3,3,3,3,3,3,3,3,3,3"))
        list.add(Achievements(4,ctx.resources.getString(R.string.p_title_hero),0,0,2,"2,6,10,14,20,30,40,50,60,80,100"))
        list.add(Achievements(5,ctx.resources.getString(R.string.p_title_legenda),0,0,2,"2,6,10,14,20,30,40,50,60,80,100"))
        list.add(Achievements(6,ctx.resources.getString(R.string.p_title_pobeditel),0,0,6,"6,6,6,6,6,6,6,6,6,6,6"))
        achievDao.insertList(list)
    }



}