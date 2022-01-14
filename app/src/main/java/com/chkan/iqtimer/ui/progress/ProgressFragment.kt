package com.chkan.iqtimer.ui.progress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.activityViewModels
import com.chkan.iqtimer.MainActivity
import com.chkan.iqtimer.R
import com.chkan.iqtimer.databinding.FragmentProgressBinding
import com.chkan.iqtimer.domain.models.Goal
import com.chkan.iqtimer.ui.progress.dialogs.ConfirmDeleteGoalDialog
import com.chkan.iqtimer.ui.progress.vm.ProgressViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProgressFragment : Fragment() {

    private val viewModel: ProgressViewModel by activityViewModels()
    @Inject lateinit var goal: Goal

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProgressBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.goal = goal

        return binding.root
    }

     override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

         v.findViewById<ImageButton>(R.id.goal_btn_add).setOnClickListener {
             (activity as MainActivity).getBottomSheet()
         }

         v.findViewById<ImageButton>(R.id.goal_btn_cancel).setOnClickListener {
             ConfirmDeleteGoalDialog().show(
                 childFragmentManager, ConfirmDeleteGoalDialog.TAG)
         }

         viewModel.newGoalLiveData.observe(this,{
             goal.setNewGoal(it)
         })

         viewModel.deleteGoalLiveData.observe(this,{
             if(it) {goal.deleteGoal(resources.getString(R.string.goal_name_empty),resources.getString(R.string.goal_desc_empty))}
         })

    }
}