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

    private val _isPremium: MutableLiveData<Boolean> = MutableLiveData()
    val isPremium: LiveData<Boolean>
        get() = _isPremium

    val achievLiveData: LiveData<List<Achievements>> = achievUseCase.achievementsFlow.asLiveData()

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
            try {
                _isPremium.postValue(progressUseCase.isPremium())
                progressUseCase.checkGoalExpired()
            } catch (e:Exception){
                Log.d("MYAPP", "ProgressViewModel - checkGoalExpired(): ${e.message}")
            }
        }
    }

    fun setPremium(b: Boolean) {
        progressUseCase.setPremium(b)
        _isPremium.value = b
    }

}