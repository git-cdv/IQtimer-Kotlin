package com.chkan.iqtimer.utils

import java.util.*


fun String.toTimerFormat() : String {
    val minutes = this.toInt()
    return if (minutes >= 60) { //если время отчета равно или больше 1 часа, то формат с часами
        String.format(
            Locale.getDefault(), "%02d:%02d:%02d", minutes / 60,
            minutes % 60, 0
        )
    } else { //формат с минутами и секундами
        String.format(Locale.getDefault(), "%02d:%02d", minutes, 0)
    }
}