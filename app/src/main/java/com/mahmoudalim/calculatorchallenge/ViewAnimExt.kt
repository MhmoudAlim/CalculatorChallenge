package com.mahmoudalim.calculatorchallenge

import android.view.View
import android.view.animation.AnimationUtils
import androidx.interpolator.view.animation.FastOutLinearInInterpolator



fun View.slideInRight(animTime: Long, startOffset: Long){
    val slideInRight = AnimationUtils.loadAnimation(context, R.anim.slide_in_right).apply {
        duration = animTime
        interpolator = FastOutLinearInInterpolator()
        this.startOffset = startOffset
    }
    startAnimation(slideInRight)
}


