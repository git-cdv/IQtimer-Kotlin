package com.chkan.iqtimer.utils

sealed class MyResult{
    object Success : MyResult()
    data class Error (val message : String, val withDialog : Boolean) : MyResult()
}
