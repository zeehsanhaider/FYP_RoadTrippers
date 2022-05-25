package com.example.roadtrippers.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.roadtrippers.R
import com.google.android.material.button.MaterialButton

class TripBookedSuccessDialog(
    private val context: Context,
    private val callback: TripBookedSuccessDialog.DialogCallback
) {
    private var dialog: Dialog

    interface DialogCallback {
        fun onDialogDismiss()
    }

    private lateinit var btnOk: MaterialButton

    init {
        dialog = getDialog()
    }

    private fun getDialog(): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_trip_booked_success)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)

        btnOk = dialog.findViewById(R.id.btn_confirm)

        btnOk.setOnClickListener {
            callback.onDialogDismiss()
            dialog.dismiss()
        }
        return dialog
    }

    fun showDialog() {
        dialog.show()
    }
}