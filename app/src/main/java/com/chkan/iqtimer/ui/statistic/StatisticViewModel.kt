package com.chkan.iqtimer.ui.statistic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chkan.iqtimer.data.room.DatabaseModel
import com.chkan.iqtimer.domain.models.ChartModel
import com.chkan.iqtimer.domain.usecases.StatisticUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val statisticUseCase : StatisticUseCase
) : ViewModel() {

    private val _countToday: MutableLiveData<Int> = MutableLiveData()
    val countToday: LiveData<Int>
        get() = _countToday

    private val _countWeek: MutableLiveData<Int> = MutableLiveData()
    val countWeek: LiveData<Int>
        get() = _countWeek

    private val _countMonth: MutableLiveData<Int> = MutableLiveData()
    val countMonth: LiveData<Int>
        get() = _countMonth

    private val _countTotal: MutableLiveData<Int> = MutableLiveData()
    val countTotal: LiveData<Int>
        get() = _countTotal

    private val _listTotal: MutableLiveData<List<DatabaseModel>> = MutableLiveData()
    val listTotal: LiveData<List<DatabaseModel>>
        get() = _listTotal

    private val _isDataObzorDone: MutableLiveData<Boolean> = MutableLiveData()
    val isDataObzorDone: LiveData<Boolean>
        get() = _isDataObzorDone

    val isDataDaysDone: MutableLiveData<Boolean> = MutableLiveData()
    val isDataMonthDone: MutableLiveData<Boolean> = MutableLiveData()

    private val _dataDays: MutableLiveData<ChartModel> = MutableLiveData()
    val dataDaysLiveData: LiveData<ChartModel>
        get() = _dataDays

    private val _dataMonth: MutableLiveData<ChartModel> = MutableLiveData()
    val dataMonthLiveData: LiveData<ChartModel>
        get() = _dataMonth

    init {
        getDataObzor()
        getDataDays()
        getDataMonth()
    }

    private fun getDataObzor() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val current = statisticUseCase.getCountToday()
                _countToday.postValue(current)
                _countWeek.postValue(statisticUseCase.getCountWeek()+current)
                _countMonth.postValue(statisticUseCase.getCountMonth()+current)
                _countTotal.postValue(statisticUseCase.getCountTotal()+current)
                _isDataObzorDone.postValue(true)
            }catch (e:Exception){
                Log.d("MYAPP", "StatisticViewModel - getDataObzor() ERROR: ${e.message}")
            }
        }
    }

    private fun getDataDays() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _dataDays.postValue(statisticUseCase.getDataDays())
            }catch (e:Exception){
                Log.d("MYAPP", "StatisticViewModel - getDataDays() ERROR: ${e.message}")
            }
        }
    }

    private fun getDataMonth() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _dataMonth.postValue(statisticUseCase.getDataMonth())
            }catch (e:Exception){
                Log.d("MYAPP", "StatisticViewModel - getDataMonth() ERROR: ${e.message}")
            }
        }
    }

    fun getListTotal() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _listTotal.postValue(statisticUseCase.getListFull())
            }catch (e:Exception){
                Log.d("MYAPP", "StatisticViewModel - getListTotal() ERROR: ${e.message}")
            }
        }
    }

}