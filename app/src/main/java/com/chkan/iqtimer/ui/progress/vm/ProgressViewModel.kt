package com.chkan.iqtimer.ui.progress.vm

import android.util.Log
import androidx.lifecycle.*
import com.chkan.iqtimer.data.room.Achievements
import com.chkan.iqtimer.domain.models.GoalModel
import com.chkan.iqtimer.domain.usecases.AchievementsUseCase
import com.chkan.iqtimer.domain.usecases.ProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val progressUseCase : ProgressUseCase,
    achievUseCase : AchievementsUseCase
): ViewModel() {

    private val _deleteGoalLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val deleteGoalLiveData: LiveData<Boolean>
        get() = _deleteGoalLiveData

    val isPremium: MutableLiveData<Boolean> = MutableLiveData()

    val achievLiveData: LiveData<List<Achievements>> = achievUseCase.achievementsFlow.asLiveData()

    init {
        checkPremium()
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

    fun checkGoal() {
        viewModelScope.launch(Dispatchers.IO) {
            progressUseCase.checkGoalExpired()
        }
    }

    fun setPremium(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            progressUseCase.setPremium(state)
        }
        isPremium.value = state
    }

    private fun checkPremium() {
        isPremium.value = progressUseCase.getPremium()
    }

}