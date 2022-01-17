package com.chkan.iqtimer.ui.progress.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chkan.iqtimer.domain.models.GoalModel
import com.chkan.iqtimer.domain.usecases.ProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val progressUseCase : ProgressUseCase
): ViewModel() {

    private val _newGoalLiveData: MutableLiveData<GoalModel> = MutableLiveData()
    val newGoalLiveData: LiveData<GoalModel>
        get() = _newGoalLiveData

    private val _deleteGoalLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val deleteGoalLiveData: LiveData<Boolean>
        get() = _deleteGoalLiveData

    private val _counterLiveData: MutableLiveData<Int> = MutableLiveData()
    val counterLiveData: LiveData<Int>
        get() = _counterLiveData

    init {
        _counterLiveData.value = progressUseCase.getCounter()
    }

    fun setNewGoal(goal: GoalModel) {
        _newGoalLiveData.value = goal
    }

    fun deleteGoal() {
        _deleteGoalLiveData.value = true
    }
}