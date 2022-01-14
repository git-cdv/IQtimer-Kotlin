package com.chkan.iqtimer.ui.progress.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.chkan.iqtimer.R
import com.chkan.iqtimer.ui.progress.vm.ProgressViewModel

class ConfirmDeleteGoalDialog : DialogFragment() {

    private val viewModel: ProgressViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.dlg_delete_goal_title))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteGoal()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                dismiss()
            }
            .create()

    companion object {
        const val TAG = "ConfirmDeleteGoalDialog"
    }
}