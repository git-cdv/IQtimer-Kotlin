package com.chkan.iqtimer.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.preference.*
import androidx.preference.EditTextPreference.OnBindEditTextListener
import com.chkan.iqtimer.R
import com.chkan.iqtimer.domain.models.Session
import com.chkan.iqtimer.ui.main.NotifManager.Companion.CHANNEL_ID_BREAK
import com.chkan.iqtimer.ui.main.NotifManager.Companion.CHANNEL_ID_SESSION
import com.chkan.iqtimer.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    @Inject
    lateinit var session: Session

    private lateinit var pref: SharedPreferences
    private var editTextTime : EditTextPreference? = null
    private var editTextBreak : EditTextPreference? = null
    private var editTextPlan : EditTextPreference? = null

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

        preferenceManager.findPreference<Preference>("set_sound")?.onPreferenceClickListener = this
        preferenceManager.findPreference<Preference>("set_sound_break")?.onPreferenceClickListener = this

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
            SP_DEFAULT_TIME-> {
                val time = pref.getString(SP_DEFAULT_TIME,"50")
                editTextTime?.summary = time + " " + getString(R.string.minut)
                if (time != null) {
                    session.timeDefault = time
                    if(session.stateLiveData.value != State.ACTIVE || session.stateLiveData.value != State.BREAK){
                        session.timeLiveData.value=time.toTimerFormat()
                    }
                    session.settingsUpdatedLiveData.value = Pair(SET_UPD_TIME,time)
                }
            }
            SP_DEFAULT_BREAK -> {
                val breakTime = pref.getString(SP_DEFAULT_BREAK,"15")
                editTextBreak?.summary = breakTime  + " " + getString(R.string.minut)
                if (breakTime != null) {
                    session.breakDefault = breakTime
                    session.settingsUpdatedLiveData.value = Pair(SET_UPD_BREAK,breakTime)
                }
            }

            SP_DEFAULT_PLAN -> {
                val plan = pref.getString(SP_DEFAULT_PLAN,"8")
                editTextPlan?.summary =  plan + " " + getString(R.string.sessiy)
                session.planLiveData.value = plan?.toInt()
            }
            SP_SWITCH_OUT -> {
                val state = pref.getBoolean(SP_SWITCH_OUT,false)
                session.notifSoundOut = state
            }
            }
        }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference?.key){
            "set_sound" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID_SESSION)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context?.packageName)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, R.string.no_supported, Toast.LENGTH_LONG).show()
                }
            }
            "set_sound_break" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID_BREAK)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context?.packageName)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, R.string.no_supported, Toast.LENGTH_LONG).show()
                }
            }
        }
        return true
    }

    //слушает изменение одной конкретной настройки ДО ее записи (для проверки на валидность)
    //если метод возвращает true - значение будет записано, false - нет
    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val toast: Toast = Toast.makeText(context, R.string.sett_e_tost, Toast.LENGTH_LONG)

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