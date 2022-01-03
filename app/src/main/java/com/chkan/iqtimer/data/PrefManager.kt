package com.chkan.iqtimer.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.chkan.iqtimer.utils.*
import javax.inject.Inject

class PrefManager @Inject constructor (val context: Context) {

    val pref: SharedPreferences = context.getSharedPreferences("app_pref",MODE_PRIVATE)

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

    fun getDefaultPlan(): Int {
        return pref.getInt(SP_DEFAULT_PLAN,8)
    }

    fun getDefaultTime(): Int {
        return pref.getInt(SP_DEFAULT_TIME,50)
    }

    fun getCurrentCount(): Int {
        return pref.getInt(SP_CURRENT_COUNT,0)
    }

}