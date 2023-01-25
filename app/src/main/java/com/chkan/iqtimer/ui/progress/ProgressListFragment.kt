package com.chkan.iqtimer.ui.progress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chkan.iqtimer.databinding.FragmentProgressListBinding
import com.chkan.iqtimer.ui.progress.vm.ProgressViewModel

class ProgressListFragment : Fragment() {

    private val viewModel: ProgressViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProgressListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        return binding.root
    }
}