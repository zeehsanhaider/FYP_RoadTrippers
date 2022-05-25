package com.example.letsgoadmin.util.extension

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letsgoadmin.R

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun setLinearLayoutManager(context: Context, rv: RecyclerView, isVertical: Boolean) {

    if(isVertical) {
        rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    } else {
        rv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

}

fun EditText.isEmpty(): Boolean {
    if(text.toString().isEmpty()) {
        return true
    }
    return false
}

fun EditText.containsDigits(): Boolean {
    for(char in text.toString().toCharArray()) {
        if(char in '0'..'9') {
            return true
        }
    }
    return false
}

fun Context.getLoadingDialog(
    title: String,
    description: String
): Dialog {
    val dialog = Dialog(this)
    dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_loading)
    val dialogTitle = dialog.findViewById(R.id.tx_title) as TextView
    val dialogDescription = dialog.findViewById(R.id.tx_desc) as TextView
    dialogTitle.text = title
    dialogDescription.text = description
    return dialog
}
