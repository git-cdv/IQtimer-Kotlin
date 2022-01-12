package com.chkan.iqtimer.data.room

import androidx.room.*

@Dao
interface HistoryDao {

    @Query("SELECT * FROM databasemodel LIMIT 7")
    fun getWeek(): List<DatabaseModel>

    @Query("SELECT * FROM databasemodel LIMIT 31")
    fun getMonth(): List<DatabaseModel>

    @Query("SELECT * FROM databasemodel")
    fun getTotal(): List<DatabaseModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert( item: DatabaseModel)

}

@Database(entities = [DatabaseModel::class], version = 1)
abstract class HistoryDatabase: RoomDatabase() {
    abstract val historyDao: HistoryDao
}
