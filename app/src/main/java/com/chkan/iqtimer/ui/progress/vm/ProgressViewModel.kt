package com.chkan.iqtimer.ui.progress.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chkan.iqtimer.domain.models.GoalModel

class ProgressViewModel: ViewModel() {

    private val _newGoalLiveData: MutableLiveData<GoalModel> = MutableLiveData()
    val newGoalLiveData: LiveData<GoalModel>
        get() = _newGoalLiveData

    fun setNewGoal(goal: GoalModel) {
        _newGoalLiveData.value = goal
    }
}