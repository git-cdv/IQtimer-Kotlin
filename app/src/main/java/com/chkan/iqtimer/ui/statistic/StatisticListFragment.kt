package com.chkan.iqtimer.ui.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chkan.iqtimer.R
import com.chkan.iqtimer.ui.statistic.adapter.StatisticAdapter
import com.chkan.iqtimer.ui.statistic.vm.StatisticViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticListFragment : Fragment() {

    private val viewModel: StatisticViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistic_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_statistic)
        val adapter = StatisticAdapter()
        viewModel.getListTotal()
        viewModel.listTotal.observe(this,{
            adapter.setList(it)
            recyclerView.adapter = adapter
        })
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_list)
        fab.alpha = 0.5F
        fab.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}