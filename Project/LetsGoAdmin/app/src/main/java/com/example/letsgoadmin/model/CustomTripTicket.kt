package com.example.letsgoadmin.model

import android.os.Parcelable
import com.example.letsgoadmin.util.constant.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomTripTicket(
    var id: String = Constants.NULL_STRING,
    // Instead of trip its place here
    var userId: String = Constants.NULL_STRING,
    var placeId: String = Constants.NULL_STRING,
    var vehicleId: String = Constants.NULL_STRING,

    var totalExpense: Int = Constants.NULL_INT,
    var contact: String = Constants.NULL_STRING,
    var persons: Int = Constants.NULL_INT,
    var dateCreated: Long = Constants.NULL_LONG,

    // Custom trip tickets adds the following fields
    // than Default trip ticket
    var days: Int = Constants.NULL_INT,
    var departureDate: String = Constants.NULL_STRING,
    var departureTime: String = Constants.NULL_STRING
) : Parcelable