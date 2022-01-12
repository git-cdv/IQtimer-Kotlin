package com.chkan.iqtimer.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.chkan.iqtimer.MainActivity
import com.chkan.iqtimer.R
import com.chkan.iqtimer.databinding.FragmentMainBinding
import com.chkan.iqtimer.domain.models.Session
import com.chkan.iqtimer.utils.State
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.session = session

        session.planLiveData.observe(this,{
            binding.stepProgressBar.numDots = it
        })

        session.stateLiveData.observe(this,{
            when(it){
                State.STOPED -> session.setTimeDefault()
            }
        })

        session.countLiveData.observe(this,{
            val plan = session.planLiveData.value
            if (plan!=null) {
                //если count придет больше размера бара - будет краш
                binding.stepProgressBar.currentProgressDot = if (it < plan) it-1 else plan-1
            }
        })

        binding.mainTvTimer.setOnClickListener {
            if(session.stateLiveData.value == State.ACTIVE){
                (activity as MainActivity).stopTimer(true)
                session.stateLiveData.value = State.PAUSED
            } else {
                (activity as MainActivity).startTimer()
                session.stateLiveData.value = State.ACTIVE
            }
        }

        binding.mainBtnStop.setOnClickListener {
            (activity as MainActivity).stopTimer(false)
            session.stateLiveData.value = State.STOPED
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<ImageButton>(R.id.main_btn_menu).setOnClickListener {
            showMenu(it)
        }
    }

    private fun showMenu(v: View) {
        val popup = PopupMenu(requireContext(),v)
        popup.inflate(R.menu.popup_menu)

        popup.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_progress -> {
                    findNavController().navigate(R.id.action_mainFragment_to_progressFragment)
                }
                R.id.menu_statistic -> {
                    findNavController().navigate(R.id.action_mainFragment_to_statisticFragment)
                }
                R.id.menu_setting -> {
                    findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
                }
                R.id.menu_about -> {
                    findNavController().navigate(R.id.action_mainFragment_to_aboutFragment)
                }
            }
            true
        }
        popup.show()
    }
}