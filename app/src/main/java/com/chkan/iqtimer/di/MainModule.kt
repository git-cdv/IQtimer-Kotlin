package com.chkan.iqtimer.di

import android.content.Context
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.data.room.AchievDao
import com.chkan.iqtimer.domain.models.Goal
import com.chkan.iqtimer.ui.main.NotifManager
import com.chkan.iqtimer.domain.models.Session
import com.chkan.iqtimer.domain.usecases.AchievementsUseCase
import com.chkan.iqtimer.domain.usecases.ProgressUseCase
import com.chkan.iqtimer.ui.statistic.ChartManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MainModule {

    @Provides
    @Singleton
    fun providePreference(@ApplicationContext appContext: Context): PrefManager {
               return PrefManager(appContext)
    }

    @Provides
    fun provideProgress(pref: PrefManager, goal: Goal) : ProgressUseCase {
        return ProgressUseCase(pref,goal)
    }

    @Provides
    @Singleton
    fun provideAchievements(@ApplicationContext appContext: Context, achievDao: AchievDao) : AchievementsUseCase {
        return AchievementsUseCase(appContext,achievDao)
    }

    @Provides
    @Singleton
    fun provideSession(pref: PrefManager, progressUseCase: ProgressUseCase) : Session {
        return Session(pref,progressUseCase)
    }

    @Provides
    @Singleton
    fun provideGoal(pref: PrefManager) : Goal {
        return Goal(pref)
    }

    @Provides
    @Singleton
    fun provideNotif(@ApplicationContext appContext: Context): NotifManager {
        return NotifManager(appContext)
    }

    @Provides
    @Singleton
    fun provideChartManager(@ApplicationContext appContext: Context) : ChartManager {
        return ChartManager(appContext)
    }

}