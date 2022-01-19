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
    val level: Int,
    @ColumnInfo(name = "Current")
    val current: Int,
    @ColumnInfo(name = "Plan")
    val plan: Int,
    @ColumnInfo(name = "PlanAsArray")
    val planAsArray: String
)