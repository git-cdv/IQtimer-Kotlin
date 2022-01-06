package com.chkan.iqtimer.di

import android.content.Context
import androidx.room.Room
import com.chkan.iqtimer.data.room.HistoryDao
import com.chkan.iqtimer.data.room.HistoryDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): HistoryDatabase {

        return Room.databaseBuilder(
            appContext,
            HistoryDatabase::class.java,
            "history"
        ).build()
    }

    @Provides
    fun provideHistoryDao(db: HistoryDatabase): HistoryDao {
        return db.historyDao
    }
}