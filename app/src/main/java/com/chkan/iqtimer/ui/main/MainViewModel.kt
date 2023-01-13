package com.chkan.iqtimer.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chkan.iqtimer.domain.usecases.AchievementsUseCase
import com.chkan.iqtimer.domain.usecases.SessionsUseCase
import com.chkan.iqtimer.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionsUseCase: SessionsUseCase,
    private val achievUseCase: AchievementsUseCase
): ViewModel(){

    init {
        checkFirst()
    }

    val isFirstLiveData = SingleLiveEvent<Boolean>()

    fun checkWorkDate() {
        viewModelScope.launch(Dispatchers.IO) {
            sessionsUseCase.checkWorkDate()
        }
    }

    private fun checkFirst() {
        viewModelScope.launch (Dispatchers.IO) {
            if(sessionsUseCase.isFirst()){
                isFirstLiveData.postValue(true)
                sessionsUseCase.startFirst()
                achievUseCase.initAchievements()
            } else {
                isFirstLiveData.postValue(true)
            }
        }
    }
}