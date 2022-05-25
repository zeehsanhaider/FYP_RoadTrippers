package com.example.letsgoadmin.util.navigation

import android.os.CountDownTimer
import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.Navigation.findNavController

object NavigationUtils {

    const val SCREEN_DELAY = 2000L

    fun navigate(view: View?, @IdRes navigationId: Int) {
        findNavController(view!!).navigate(navigationId)
    }

    fun navigateBack(view: View?) {
        findNavController(view!!).popBackStack()
    }

    fun navigateForwardAfterDelay(view: View?, @IdRes navigationId: Int) {
        object : CountDownTimer(SCREEN_DELAY, SCREEN_DELAY / 4) {
            override fun onTick(l: Long) {}
            override fun onFinish() {
                navigate(view, navigationId)
            }
        }.start()
    }

    fun navigateBackAfterDelay(view: View?) {
        object : CountDownTimer(SCREEN_DELAY, SCREEN_DELAY / 4) {
            override fun onTick(l: Long) {}
            override fun onFinish() {
                navigateBack(view)
            }
        }.start()
    }

}