package com.chkan.iqtimer.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.preference.*
import androidx.preference.EditTextPreference.OnBindEditTextListener
import com.chkan.iqtimer.R
import com.chkan.iqtimer.utils.*
import java.lang.NumberFormatException

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private lateinit var pref: SharedPreferences
    private var editTextTime : EditTextPreference? = null
    private var editTextBreak : EditTextPreference? = null
    private var editTextPlan : EditTextPreference? = null
    private var switchSound : SwitchPreference? = null
    private var setWorkSound : Preference? = null
    private var setBreakSound : Preference? = null
    private var setSilentSound : SwitchPreference? = null


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        pref = PreferenceManager.getDefaultSharedPreferences(context)
        pref.registerOnSharedPreferenceChangeListener(this)

        //вытаскиваем EditTextPreference, сетим default summary и назначаем его InputType.TYPE_CLASS_NUMBER
        editTextTime = preferenceManager.findPreference(SP_DEFAULT_TIME)
        editTextTime?.summary = pref.getString(SP_DEFAULT_TIME,"50") + " " + getString(R.string.minut)

        editTextBreak = preferenceManager.findPreference(SP_DEFAULT_BREAK)
        editTextBreak?.summary = pref.getString(SP_DEFAULT_BREAK,"15") + " " + getString(R.string.minut)

        editTextPlan = preferenceManager.findPreference(SP_DEFAULT_PLAN)
        editTextPlan?.summary = pref.getString(SP_DEFAULT_PLAN,"8") + " " + getString(R.string.sessiy)

        switchSound = preferenceManager.findPreference(SP_SWITCH_SOUND)
        setWorkSound = preferenceManager.findPreference("SETTING_work_sound")
        setBreakSound = preferenceManager.findPreference("SETTING_break_sound")
        setSilentSound = preferenceManager.findPreference("SETTING_Switch_outsilent")

        val editTextListener = OnBindEditTextListener { editText: EditText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.text.clear() //очищаем фокус для удобства ввода
        }

        editTextTime?.setOnBindEditTextListener(editTextListener)
        editTextBreak?.setOnBindEditTextListener(editTextListener)
        editTextPlan?.setOnBindEditTextListener(editTextListener)

        val preferencePlan = findPreference<Preference>("DEFAULT_Plan")
        preferencePlan?.onPreferenceChangeListener = this
    }

    //слушает изменение в настройках ПОСЛЕ записи
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when(key){
            SP_DEFAULT_TIME-> editTextTime?.summary = pref.getString(SP_DEFAULT_TIME,"50") + " " + getString(R.string.minut)
            SP_DEFAULT_BREAK -> editTextBreak?.summary = pref.getString(SP_DEFAULT_BREAK,"15") + " " + getString(R.string.minut)
            SP_DEFAULT_PLAN -> editTextPlan?.summary = pref.getString(SP_DEFAULT_PLAN,"8") + " " + getString(R.string.sessiy)
            SP_SWITCH_SOUND -> {
                val switch = pref.getBoolean(SP_SWITCH_SOUND,true)
                    setWorkSound?.isVisible = switch
                    setBreakSound?.isVisible = switch
                    setSilentSound?.isVisible = switch
                }
            }
        }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        Log.d("MYAPP", "onPreferenceClick - preference: ${preference?.key}")
        return true
    }

    //слушает изменение одной конкретной настройки ДО ее записи (для проверки на валидность)
    //если метод возвращает true - значение будет записано, false - нет
    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val toast: Toast = Toast.makeText(context, R.string.sett_e_tost, Toast.LENGTH_LONG)

        Log.d("MYAPP", "onPreferenceChange - value: $newValue")
        if (preference?.key == "DEFAULT_Plan") {
            val input = newValue as String
            try {
                if (input.toInt() > 30 || input.toInt() < 1) {
                    toast.show()
                    return false
                }
            } catch (e: NumberFormatException) {
                toast.show()
                return false
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        pref.registerOnSharedPreferenceChangeListener(this)
    }
}