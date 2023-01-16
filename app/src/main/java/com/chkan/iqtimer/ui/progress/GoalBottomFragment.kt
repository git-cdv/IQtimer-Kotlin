package com.chkan.iqtimer.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chkan.iqtimer.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.chkan.iqtimer.domain.models.GoalModel
import com.chkan.iqtimer.ui.progress.vm.ProgressViewModel
import com.google.android.material.textfield.TextInputEditText


class GoalBottomFragment : BottomSheetDialogFragment() {

    private val viewModel: ProgressViewModel by activityViewModels()
    var selectedType = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.goal_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        val radioGroup = view.findViewById<RadioGroup>(R.id.goal_radioGroup)
        val radioSes = view.findViewById<RadioButton>(R.id.radioSessions)
        val et_plan = view.findViewById<TextInputEditText>(R.id.goal_et_quantity)
        val et_days = view.findViewById<TextInputEditText>(R.id.goal_et_days)
        val et_name = view.findViewById<TextInputEditText>(R.id.goal_name)
        val til_plan = view.findViewById<TextInputLayout>(R.id.goal_til_quantity)
        val til_days = view.findViewById<TextInputLayout>(R.id.goal_til_days)

        view.findViewById<Button>(R.id.goal_btn_cancel).setOnClickListener {
            dismiss()
        }
        view.findViewById<Button>(R.id.goal_btn_create).setOnClickListener {
            if(validateInput(et_plan,til_plan, et_days, til_days)){
                val name = et_name.text.toString()
                val plan = et_plan.text.toString()
                selectedType = if(radioGroup.checkedRadioButtonId==radioSes.id) 0 else 1
                val _type = if(selectedType == 0) resources.getString(R.string.sessiy) else resources.getString(R.string.powerdays)
                val days = et_days.text.toString()
                val desc =
                    "${resources.getString(R.string.text_my_goal)} $plan $_type ${resources.getString(
                        R.string.text_in
                    )} $days ${resources.getString(R.string.days)}."

                viewModel.setNewGoal(GoalModel(name,desc,plan.toInt(),days.toInt(),selectedType))
                dismiss()
            }

        }
    }

    private fun validateInput(quantity: TextInputEditText, til_quantity: TextInputLayout,  days: TextInputEditText , til_days: TextInputLayout ): Boolean {
        return when {
            quantity.text.isNullOrEmpty() -> {
                til_quantity.error = resources.getString(R.string.empty)
                quantity.doAfterTextChanged { til_quantity.error = null }
                false
            }
            days.text.isNullOrEmpty() -> {
                til_days.error = resources.getString(R.string.empty)
                days.doAfterTextChanged { til_days.error = null }
                false
            }
            else -> {
                true
            }
        }
    }
}