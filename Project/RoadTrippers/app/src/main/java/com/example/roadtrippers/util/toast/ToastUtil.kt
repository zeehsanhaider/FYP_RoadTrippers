package com.example.roadtrippers.util.toast

import android.content.Context
import android.widget.Toast

class ToastUtil(private val context: Context) {

    fun longToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun shortToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}