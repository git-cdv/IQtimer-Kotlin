package com.chkan.iqtimer.domain.usecases

import com.chkan.iqtimer.data.PrefManager
import javax.inject.Inject

class ProgressUseCase @Inject constructor(private val prefManager: PrefManager) {

    fun getCounter() : Int{
        return prefManager.getCounter()
    }

}