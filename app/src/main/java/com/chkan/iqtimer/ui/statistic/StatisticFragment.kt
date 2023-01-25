package com.chkan.iqtimer.ui.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chkan.iqtimer.R
import com.chkan.iqtimer.databinding.FragmentStatisticBinding
import com.chkan.iqtimer.ui.statistic.vm.StatisticViewModel
import com.github.mikephil.charting.charts.BarChart
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatisticFragment : Fragment() {

    private val viewModel: StatisticViewModel by activityViewModels()
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
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        chartDay = view.findViewById(R.id.history_chart_days)
        chartMonth = view.findViewById(R.id.history_chart_month)
        viewModel.getData()
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.alpha = 0.5F
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_statisticFragment_to_statisticListFragment)
        }
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

