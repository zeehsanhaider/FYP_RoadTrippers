package com.example.letsgoadmin.util.animation

import android.view.View
import androidx.core.widget.NestedScrollView

object Tools {

    fun nestedScrollTo(nested: NestedScrollView, targetView: View) {
        nested.post { nested.scrollTo(500, targetView.bottom) }
    }

}