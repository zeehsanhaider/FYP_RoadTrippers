package com.example.letsgoadmin.model

import android.os.Parcelable
import com.example.letsgoadmin.util.constant.Constants.NULL_INT
import com.example.letsgoadmin.util.constant.Constants.NULL_LONG
import com.example.letsgoadmin.util.constant.Constants.NULL_STRING
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trip(

    var id: String = NULL_STRING,
    var title: String = NULL_STRING,
    var description: String = NULL_STRING,
    var imageUrls: String = NULL_STRING,

    // After wards we will add a 1,2,3 days and week duration trip
    // User can search trips by their duration
    var tripDays: Int = NULL_INT,
    var departureDate: String = NULL_STRING,
    var departureTime: String = NULL_STRING,

    var dateCreated: Long = NULL_LONG,
    var isDeleted: Boolean = false,

    var maxSeats: Int = NULL_INT,
    var bookedSeats: Int = NULL_INT,
    var remainingSeats: Int = NULL_INT,

    var expensePerPerson: Int = NULL_INT,
    var totalExpenseGathered: Int = NULL_INT,

    var bookedUsers: String = NULL_STRING
) : Parcelable