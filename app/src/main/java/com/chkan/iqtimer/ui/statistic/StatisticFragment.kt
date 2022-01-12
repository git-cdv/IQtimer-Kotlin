package com.chkan.iqtimer.ui.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.chkan.iqtimer.databinding.FragmentStatisticBinding
import com.github.mikephil.charting.charts.BarChart
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
        binding.fab.alpha = 0.5F

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.dataDaysLiveData.observe(this,{
            chartManager.getChartDays(chartDay,it.data,it.titles,it.default).invalidate()
            viewModel.isDataDaysDone.value = true
        })

        viewModel.dataMonthLiveData.observe(this,{
            chartManager.getChartMonth(chartMonth,it.data,it.titles).invalidate()
            viewModel.isDataMonthDone.value = true
        })

    }
}

