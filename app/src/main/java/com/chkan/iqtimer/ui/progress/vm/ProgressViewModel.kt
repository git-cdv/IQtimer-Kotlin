package com.chkan.iqtimer.ui.progress.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chkan.iqtimer.domain.models.GoalModel
import com.chkan.iqtimer.domain.usecases.ProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val progressUseCase : ProgressUseCase
): ViewModel() {

    private val _deleteGoalLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val deleteGoalLiveData: LiveData<Boolean>
        get() = _deleteGoalLiveData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                progressUseCase.checkGoalExpired()
            } catch (e:Exception){
                Log.d("MYAPP", "ProgressViewModel - checkGoalExpired(): ${e.message}")
            }

        }
    }

    fun setNewGoal(goal: GoalModel) {
        progressUseCase.setNewGoal(goal)
    }

    fun deleteGoal() {
        _deleteGoalLiveData.value = true
    }

    fun deleteGoalDone() {
        _deleteGoalLiveData.value = false
    }

}