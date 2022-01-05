package com.chkan.iqtimer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.chkan.iqtimer.MainActivity
import com.chkan.iqtimer.R
import com.chkan.iqtimer.databinding.FragmentMainBinding
import com.chkan.iqtimer.domain.Session
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
        // Позволяет привязке данных наблюдать за LiveData в течение жизненного цикла этого фрагмента
        binding.lifecycleOwner = this
        // Предоставление привязки доступа к Session
        binding.session = session

        session.planLiveData.observe(this,{
            binding.stepProgressBar.numDots = it
        })

        session.countLiveData.observe(this,{
            val plan = session.planLiveData.value
            if (plan!=null) {
                //если count придет больше размера бара - будет краш
                binding.stepProgressBar.currentProgressDot = if (it < plan) it-1 else plan-1
            }
        })

        binding.mainTvTimer.setOnClickListener {
            (activity as MainActivity).startTimer()
            session.stateLiveData.value = Session.State.ACTIVE
        }

        binding.mainBtnStop.setOnClickListener {
            (activity as MainActivity).stopTimer()
            session.stateLiveData.value = Session.State.STOPED
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<ImageButton>(R.id.main_btn_menu).setOnClickListener {
          findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }
    }
}