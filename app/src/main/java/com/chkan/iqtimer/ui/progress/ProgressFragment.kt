package com.chkan.iqtimer.ui.progress

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chkan.iqtimer.MainActivity
import com.chkan.iqtimer.R
import com.chkan.iqtimer.databinding.FragmentProgressBinding
import com.chkan.iqtimer.domain.models.Goal
import com.chkan.iqtimer.ui.progress.dialogs.ConfirmDeleteGoalDialog
import com.chkan.iqtimer.ui.progress.vm.ProgressViewModel
import com.chkan.iqtimer.utils.*
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProgressFragment : Fragment() {

    private val viewModel: ProgressViewModel by activityViewModels()
    @Inject lateinit var goal: Goal
    private var timerObject : CountDownTimer? = null
    @Inject lateinit var scope: CoroutineScope
    lateinit var btnLock: ImageButton

    private val billingManager by lazy {
        BillingManager(requireContext(),requireActivity(),scope) {
            scope.launch(Dispatchers.Main) {
                if (it is MyResult.Success) {
                    viewModel.setPremium(true)
                } else {
                    if ((it as MyResult.Error).withDialog) {
                        showErrorDialog()
                    } else {
                        btnLock.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProgressBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.goal = goal
        binding.model = viewModel
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        return binding.root
    }

     override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
         viewModel.checkEffectiveCounter()
         viewModel.checkGoal()

         if(goal.isDayLeft()){
             viewLifecycleOwner.lifecycleScope.launch {
                 startGoalTimer(goal.getRestTimeLong())
             }
         }

         v.findViewById<ImageButton>(R.id.goal_btn_add).setOnClickListener {
             (activity as MainActivity).getBottomSheet()
         }

         v.findViewById<MaterialButton>(R.id.goal_done_btn).setOnClickListener {
             (activity as MainActivity).getBottomSheet()
         }

         v.findViewById<ImageButton>(R.id.goal_btn_cancel).setOnClickListener {
             ConfirmDeleteGoalDialog().show(
                 childFragmentManager, ConfirmDeleteGoalDialog.TAG)
         }

         v.findViewById<MaterialButton>(R.id.achiev_btn_more).setOnClickListener {
             findNavController().navigate(R.id.action_progressFragment_to_progressListFragment)
         }

         btnLock = v.findViewById(R.id.img_btn_lock)
         btnLock.setOnClickListener {
             billingManager.start()
             it.isEnabled = false
         }

         viewModel.deleteGoalLiveData.observe(viewLifecycleOwner) {
             if (it) {
                 goal.deleteGoal(
                     resources.getString(R.string.goal_name_empty),
                     resources.getString(R.string.goal_desc_empty)
                 )
                 viewModel.deleteGoalDone()
             }
         }

         goal.state.observe(viewLifecycleOwner) {
             when (it) {
                 GOAL_STATUS_DONE -> v.findViewById<TextView>(R.id.goal_done_text).text =
                     resources.getString(R.string.textGoalDone)
                 GOAL_STATUS_EXPIRED -> v.findViewById<TextView>(R.id.goal_done_text).text =
                     goal.getExpiredText(resources.getString(R.string.textDaysEnded))
             }
         }
     }

    private fun startGoalTimer(restTime: Long) {
        timerObject = object : CountDownTimer(restTime, 60000) {

            override fun onTick(rest: Long) {
                val hours = rest / 3600000
                val minutes = (rest % 3600000)/60000
                val h = context?.resources?.getString(R.string.h)
                val m = context?.resources?.getString(R.string.m)
                goal.timer.postValue("$hours $h $minutes $m")
            }

            override fun onFinish() {
                if(goal.state.equals(GOAL_STATUS_ACTIVE)){
                    goal.setExpiredState()
                }
            }

        }.start()
    }

    private fun showErrorDialog() {
        val builder = AlertDialog.Builder(requireContext())

        with(builder)
        {
            setTitle(":(")
            setMessage(getString(R.string.error_text))
            setPositiveButton("Ok") { _, _ -> btnLock.isEnabled = true }
            setOnCancelListener { btnLock.isEnabled = true }
            show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerObject?.cancel()
    }
}