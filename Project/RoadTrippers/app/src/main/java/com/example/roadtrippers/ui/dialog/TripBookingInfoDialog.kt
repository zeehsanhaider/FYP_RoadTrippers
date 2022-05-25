package com.example.roadtrippers.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import com.example.roadtrippers.R
import com.google.android.material.button.MaterialButton

class TripBookingInfoDialog(
    private val context: Context,
    private val callback: DialogCallback
) {

    private var dialog: Dialog

    interface DialogCallback {
        fun onDialogDismiss(noOfPersons: Int, contactNo: String)
    }

    private lateinit var edtNoOfPersons: EditText
    private lateinit var edtContactNo: EditText
    private lateinit var btnOk: MaterialButton
    private lateinit var txInfoMsg: TextView

    init {
        dialog = getDialog()
    }

    private fun getDialog(): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_trip_booking_info)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)

        edtNoOfPersons = dialog.findViewById(R.id.edt_persons)
        edtContactNo = dialog.findViewById(R.id.edt_contact)
        btnOk = dialog.findViewById(R.id.btn_confirm)
        txInfoMsg = dialog.findViewById(R.id.tx_desc)

        edtNoOfPersons.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                txInfoMsg.text = context.getString(R.string.paying_info)
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        edtContactNo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                txInfoMsg.text = context.getString(R.string.paying_info)
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        btnOk.setOnClickListener {
            if (edtNoOfPersons.text.toString().isEmpty()) {
                txInfoMsg.text = "Please specify the number of persons going to this tour"
                return@setOnClickListener
            }
            if (edtNoOfPersons.text.toString().isEmpty()) {
                txInfoMsg.text = "Please provide us with your contact number"
                return@setOnClickListener
            }

            callback.onDialogDismiss(
                edtNoOfPersons.text.toString().toInt(),
                edtContactNo.text.toString()
            )
            dialog.dismiss()
        }
        return dialog
    }

    fun showDialog() {
        dialog.show()
    }

}