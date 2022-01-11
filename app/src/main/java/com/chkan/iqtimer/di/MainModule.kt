package com.chkan.iqtimer.di

import android.content.Context
import com.chkan.iqtimer.data.PrefManager
import com.chkan.iqtimer.ui.main.NotifManager
import com.chkan.iqtimer.domain.Session
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
    @Singleton
    fun provideSession(pref: PrefManager) : Session {
        return Session(pref)
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