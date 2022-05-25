package com.example.letsgoadmin.model

import android.os.Parcelable
import com.example.letsgoadmin.util.constant.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class DefaultTripTicket(

    var id: String = Constants.NULL_STRING,
    var userId: String = Constants.NULL_STRING,
    var tripId: String = Constants.NULL_STRING,

    var totalExpense: Int = Constants.NULL_INT,
    var contact: String = Constants.NULL_STRING,
    var persons: Int = Constants.NULL_INT,
    var dateCreated: Long = Constants.NULL_LONG
) : Parcelable