package com.chkan.iqtimer.di

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
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
import kotlinx.coroutines.CoroutineScope
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
    fun provideProgress(pref: PrefManager, goal: Goal, achiv: AchievementsUseCase) : ProgressUseCase {
        return ProgressUseCase(pref,goal,achiv)
    }

    @Provides
    @Singleton
    fun provideAchievements(@ApplicationContext appContext: Context, achievDao: AchievDao) : AchievementsUseCase {
        return AchievementsUseCase(appContext,achievDao)
    }

    @Provides
    @Singleton
    fun provideSession(pref: PrefManager, progressUseCase: ProgressUseCase, applicationScope: CoroutineScope) : Session {
        return Session(pref,progressUseCase,applicationScope)
    }

    @Provides
    @Singleton
    fun provideGoal(pref: PrefManager) : Goal {
        return Goal(pref)
    }

    @Provides
    @Singleton
    fun provideNotif(@ApplicationContext appContext: Context, manager: NotificationManager): NotifManager {
        return NotifManager(appContext,manager)
    }

    @Provides
    @Singleton
    fun provideChartManager(@ApplicationContext appContext: Context) : ChartManager {
        return ChartManager(appContext)
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext appContext: Context): NotificationManager {
        return appContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

}