package com.example.letsgoadmin.util.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

object AnimationUtils {

    fun scaleAnim(
        view: View,
        fromScaleX: Float,
        fromScaleY: Float,
        toScaleX: Float,
        toScaleY: Float,
        duration: Long
    ) {
        val animationSet = AnimatorSet()
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", fromScaleX, toScaleX)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", fromScaleY, toScaleY)
        animationSet.duration = duration
        animationSet.playTogether(scaleX, scaleY)
        animationSet.start()
    }

}