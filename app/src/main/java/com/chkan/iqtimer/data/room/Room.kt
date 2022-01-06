package com.chkan.iqtimer.data.room

import androidx.room.*

@Dao
interface HistoryDao {

    @Query("select * from databasemodel")
    suspend fun getListHistory(): List<DatabaseModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert( item: DatabaseModel)
}

@Database(entities = [DatabaseModel::class], version = 1)
abstract class HistoryDatabase: RoomDatabase() {
    abstract val historyDao: HistoryDao
}
