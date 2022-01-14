package com.chkan.iqtimer.utils

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.chkan.iqtimer.R

@BindingAdapter("app:animationOnPause")
fun animationOnPause(v: TextView, value: Boolean) {
    val animTimerView: Animation = AlphaAnimation(0.2f, 1.0f) //анимация альфа канала (прозрачности от 0 до 1)
    animTimerView.duration = 800 //длительность анимации
    animTimerView.startOffset = 50 //сдвижка начала анимации (с середины)
    animTimerView.repeatMode = Animation.REVERSE //режим повтора - сначала или в обратном порядке
    animTimerView.repeatCount = Animation.INFINITE //режим повтора (бесконечно)

    if (value) {
        v.startAnimation(animTimerView)
    } else {
        v.clearAnimation()
    }
}
