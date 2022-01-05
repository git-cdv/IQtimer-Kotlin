package com.chkan.iqtimer.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.chkan.iqtimer.utils.*
import javax.inject.Inject

class PrefManager @Inject constructor (private val context: Context) {

    private val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun isFirst(): Boolean{
        return pref.getBoolean(SP_IS_FIRST,true)
    }

    fun add (key:String, value: Boolean){
        pref.edit().putBoolean(key,value).apply()
    }

    fun add (key:String, value: String){
        pref.edit().putString(key,value).apply()
    }

    fun add (key:String, value: Int){
        pref.edit().putInt(key,value).apply()
    }

    fun getString (key : String): String? {
        return pref.getString(key,null)
    }

    fun getDefaultPlan(): Int? {
        return pref.getString(SP_DEFAULT_PLAN,"8")?.toInt()
    }

    fun getDefaultTime(): String? {
        return pref.getString(SP_DEFAULT_TIME,"50")?.toTimerFormat()
    }

    fun getCurrentCount(): Int {
        return pref.getInt(SP_CURRENT_COUNT,0)
    }

}