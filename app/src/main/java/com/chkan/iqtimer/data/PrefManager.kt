package com.chkan.iqtimer.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.chkan.iqtimer.R
import com.chkan.iqtimer.utils.*
import org.joda.time.DateTime
import org.joda.time.LocalDate
import javax.inject.Inject

class PrefManager @Inject constructor (private val context: Context) {

    private val nameDef = context.resources.getString(R.string.goal_name_empty)
    private val descDef = context.resources.getString(R.string.goal_desc_empty)

    private val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun isFirst(): Boolean{
        return pref.getBoolean(SP_IS_FIRST,true)
    }

    fun isPremium(): Boolean{
        return pref.getBoolean(SP_IS_PREMIUM,false)
    }

    fun add (key:String, value: Boolean){
        pref.edit().putBoolean(key,value).apply()
    }

    fun add (key:String, value: Long){
        pref.edit().putLong(key,value).apply()
    }

    fun add (key:String, value: String){
        pref.edit().putString(key,value).apply()
    }

    fun add (key:String, value: Int){
        pref.edit().putInt(key,value).apply()
    }

    fun addDoneSession (value: Int){
        pref.edit().putInt(SP_CURRENT_COUNT,value).apply()
    }

    fun getMyString (key : String): String? {
        return pref.getString(key,null)
    }

    fun getBoolean (key : String): Boolean {
        return pref.getBoolean(key,false)
    }

    fun getInt(key : String): Int {
        return pref.getInt(key,0)
    }

    fun getLong(key : String): Long {
        return pref.getLong(key,0)
    }

    fun getWorkDate(): String? {
        return pref.getString(SP_WORK_DATE,null)
    }

    fun getDefaultPlan(): Int {
        return pref.getString(SP_DEFAULT_PLAN,"8")!!.toInt()
    }

    fun getDefaultTime(): String {
        return pref.getString(SP_DEFAULT_TIME,"50")!!
    }

    fun getCurrentCount(): Int {
        return pref.getInt(SP_CURRENT_COUNT,0)
    }

    fun getDefaultBreak(): String {
        return pref.getString(SP_DEFAULT_BREAK,"15")!!
    }

    fun startFirst() {
        pref.edit().putString(SP_WORK_DATE, DateTime.now().toString("yyyy-MM-dd")).putBoolean(SP_IS_FIRST,false).apply()
    }

    fun setWorkDateAndCleanSession(today: String?) {
        pref.edit().putString(SP_WORK_DATE, today).putInt(SP_CURRENT_COUNT,0).apply()
    }

    fun setNewGoal(name: String, desc: String, plan: Int, timeLong: Long, type: Int) {
        pref.edit().putString(SP_GOAL_NAME,name)
            .putString(SP_GOAL_DESC,desc)
            .putInt(SP_GOAL_PLAN,plan)
            .putLong(SP_GOAL_PLAN_TIME,timeLong)
            .putInt(SP_GOAL_TYPE, type)
            .putInt(SP_GOAL_CURRENT,0)
            .putInt(SP_GOAL_STATUS, GOAL_STATUS_ACTIVE)
            .putString(SP_GOAL_DESC,desc)
            .apply()
    }

    fun refreshGoal() {
        pref.edit().putString(SP_GOAL_NAME,nameDef).putString(SP_GOAL_DESC,descDef).putInt(SP_GOAL_PLAN,0)
            .putInt(SP_GOAL_CURRENT,0).putLong(SP_GOAL_PLAN_TIME,0L)
            .putInt(SP_GOAL_TYPE, SESSIONS).putInt(SP_GOAL_STATUS, GOAL_STATUS_INACTIVE).apply()
    }

    fun isGoalActive():Boolean {
        return pref.getInt(SP_GOAL_STATUS,0) == 1
    }

    fun getCounter(): Int {
        return pref.getInt(SP_COUNTER,0)
    }

    fun getEffectiveDate(): String? {
        return pref.getString(SP_EFFECTIVE_DATE,"2000-03-12")
    }

    fun addCounter(value: Int) {
        pref.edit().putInt(SP_COUNTER,value).apply()
    }

    fun addEffectiveDate(today: LocalDate?) {
        pref.edit().putString(SP_EFFECTIVE_DATE,today?.toString()).apply()
    }

    fun getGoalName(): String? {
        return pref.getString(SP_GOAL_NAME,nameDef)
    }

    fun getGoalDesc(): String? {
        return pref.getString(SP_GOAL_DESC,descDef)
    }

    fun getShort(c:Char):String? {
        return when(c){
                'h' -> context.resources?.getString(R.string.h)
                'm' ->context.resources?.getString(R.string.m)
                'd' ->context.resources?.getString(R.string.d)
            else -> {""}
        }

    }
    }
