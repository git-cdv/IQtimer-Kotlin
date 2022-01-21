package com.chkan.iqtimer.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Achievements (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "Title")
    val title: String,
    @ColumnInfo(name = "Level")
    var level: Int,
    @ColumnInfo(name = "Current")
    var current: Int,
    @ColumnInfo(name = "Plan")
    var plan: Int,
    @ColumnInfo(name = "PlanIndex")
    val planIndex: Int
)