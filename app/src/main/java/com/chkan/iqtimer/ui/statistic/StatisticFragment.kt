package com.chkan.iqtimer.ui.statistic

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.chkan.iqtimer.R
import com.chkan.iqtimer.databinding.FragmentStatisticBinding
import com.chkan.iqtimer.domain.usecases.SessionsUseCase
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatisticFragment : Fragment() {

    private val viewModel: StatisticViewModel by viewModels()
    lateinit var chartDay: BarChart
    lateinit var chartMonth: BarChart

    @Inject
    lateinit var chartManager: ChartManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentStatisticBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        chartDay = binding.historyChartDays
        chartMonth = binding.historyChartMonth

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.dataDaysLiveData.observe(this,{
            chartManager.getChartDays(chartDay,it.data,it.titles,it.default).invalidate()
            viewModel._isDataDaysDone.value = true
        })

        viewModel.dataMonthLiveData.observe(this,{

        })

    }
}

