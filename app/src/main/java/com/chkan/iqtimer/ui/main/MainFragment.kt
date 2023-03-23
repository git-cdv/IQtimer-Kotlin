package com.chkan.iqtimer.ui.main

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chkan.iqtimer.MainActivity
import com.chkan.iqtimer.R
import com.chkan.iqtimer.databinding.FragmentMainBinding
import com.chkan.iqtimer.domain.models.Session
import com.chkan.iqtimer.utils.State
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var session: Session

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.session = session

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mainBtnMenu.setOnClickListener {
            showMenu(it)
        }

        session.planLiveData.observe(viewLifecycleOwner) {
            binding.stepProgressBar.numDots = it
        }

        session.stateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                State.STOPED -> session.setTimeDefault()
                else -> {}
            }
        }

        session.countLiveData.observe(viewLifecycleOwner) {
            val plan = session.planLiveData.value
            if (plan != null) {
                //если count придет больше размера бара - будет краш
                binding.stepProgressBar.currentProgressDot = if (it < plan) it - 1 else plan - 1
            }
        }

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

        viewModel.isFirstLiveData.observe(viewLifecycleOwner){
            if(it) startOnboard(0)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkWorkDate()
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

    private fun startOnboard(step:Int) {
        val messages: List<String> = listOf(
            getString(R.string.tutorial_mess1),
            getString(R.string.tutorial_mess2),
            getString(R.string.tutorial_mess3)
        )
        val animTap: Animation = AnimationUtils.loadAnimation(context, R.anim.tutorial_tap)
        if (step==0) {
            binding.tutorialDot.isVisible = true
            binding.tutorialDot.animation = animTap
        }
        if (step==1) {
            moveView(binding.tutorialDot,binding.stepProgressBar)
        }
        if (step==2) {
            moveView(binding.tutorialDot,binding.countSes)
        }
        if (step==3) {
            binding.tutorialDot.clearAnimation()
            binding.tutorialDot.visibility = View.GONE
        }

        if (step<3) {
            try {
                Snackbar.make(
                    binding.mainBtnMenu,
                    messages[step],
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK") {
                        startOnboard(step+1)
                    }
                    .setAnchorView(binding.mainBtnMenu)
                    .setActionTextColor(Color.WHITE)
                    .setBackgroundTint(MaterialColors.getColor(requireContext(),R.attr.colorOnPrimary,R.color.brand_blue_900))
                    .setTextColor(MaterialColors.getColor(requireContext(),R.attr.colorPrimaryVariant,R.color.brand_blue_200))
                    .setBehavior(object : BaseTransientBottomBar.Behavior() {
                        override fun canSwipeDismissView(child: View): Boolean {
                            return false
                        }
                    })
                    .show()
            } catch (e:Exception){
                Firebase.crashlytics.recordException(e)
            }
        }
    }

    private fun moveView(viewToBeMoved: View, targetView: View) {
        val targetX: Float =
            targetView.x + targetView.width / 2 - viewToBeMoved.width / 2
        val targetY: Float =
            targetView.y + targetView.height / 2 - viewToBeMoved.height / 2

        viewToBeMoved.animate()
            .x(targetX)
            .y(targetY)
            .setDuration(500)
            .start()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}