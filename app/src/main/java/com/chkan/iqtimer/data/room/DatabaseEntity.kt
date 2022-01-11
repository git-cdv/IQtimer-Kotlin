package com.chkan.iqtimer.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "count")
    val count: Int,
    @ColumnInfo(name = "no_month")
    val noMonth: Int,
    @ColumnInfo(name = "no_day")
    val noDayOfWeek: Int,
    @ColumnInfo(name = "date_full")
    val date_full: String
)