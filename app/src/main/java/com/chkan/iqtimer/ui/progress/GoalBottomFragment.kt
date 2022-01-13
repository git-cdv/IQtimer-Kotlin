package com.chkan.iqtimer.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.chkan.iqtimer.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout

class GoalBottomFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.goal_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = listOf(resources.getString(R.string.text_Session), resources.getString(R.string.text_effectiv_days))
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (view.findViewById<TextInputLayout>(R.id.goal_field_type).editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}