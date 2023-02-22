package com.chkan.iqtimer.ui.progress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chkan.iqtimer.R
import com.chkan.iqtimer.databinding.FragmentProgressListBinding
import com.chkan.iqtimer.ui.progress.vm.ProgressViewModel
import com.chkan.iqtimer.utils.BillingManager
import com.chkan.iqtimer.utils.MyResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProgressListFragment : Fragment() {

    private val viewModel: ProgressViewModel by activityViewModels()
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
        val binding = FragmentProgressListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        btnLock = binding.imgBtnLock
        binding.imgBtnLock.setOnClickListener {
            billingManager.start()
            it.isEnabled = false
        }
        return binding.root
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

}