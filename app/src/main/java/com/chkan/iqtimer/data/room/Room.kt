package com.chkan.iqtimer.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM databasemodel ORDER BY id DESC LIMIT 7")
    fun getWeek(): List<DatabaseModel>

    @Query("SELECT * FROM databasemodel ORDER BY id DESC LIMIT 31")
    fun getMonth(): List<DatabaseModel>

    @Query("SELECT * FROM databasemodel")
    fun getTotal(): List<DatabaseModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert( item: DatabaseModel)

}

@Dao
interface AchievDao {

    @Query("SELECT * FROM achievements")
    fun getAchievementsFlow(): Flow<List<Achievements>>

    @Query("SELECT * FROM achievements WHERE id=:id")
    fun getAchievementForId(id:Int): Achievements

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList( list: List <Achievements>)

    @Update
    fun update(item:Achievements)

}

@Database(entities = [DatabaseModel::class, Achievements::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract val historyDao: HistoryDao
    abstract val achievDao: AchievDao
}
