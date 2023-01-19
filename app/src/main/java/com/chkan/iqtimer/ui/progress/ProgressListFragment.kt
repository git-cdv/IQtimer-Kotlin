package com.chkan.iqtimer.ui.progress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.chkan.iqtimer.R
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val upButton = view.findViewById<Button>(R.id.upButton)
        upButton.setOnClickListener{
            viewModel.up()
        }
    }
}