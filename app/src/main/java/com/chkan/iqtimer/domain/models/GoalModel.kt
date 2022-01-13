package com.chkan.iqtimer.domain.models


data class GoalModel(val isActive: Boolean, val name: String, val desc: String, val current: Int, val plan: Int, val term_current: Int, val term_plan: Int)
