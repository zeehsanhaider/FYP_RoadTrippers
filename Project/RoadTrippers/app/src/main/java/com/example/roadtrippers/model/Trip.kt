package com.example.roadtrippers.model

import android.os.Parcelable
import com.example.roadtrippers.util.constant.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trip(
    var id: String = Constants.NULL_STRING,
    var title: String = Constants.NULL_STRING,
    var description: String = Constants.NULL_STRING,
    var imageUrls: String = Constants.NULL_STRING,

    // After wards we will add a 1,2,3 days and week duration trip
    // User can search trips by their duration
    var tripDays: Int = Constants.NULL_INT,
    var departureDate: String = Constants.NULL_STRING,
    var departureTime: String = Constants.NULL_STRING,

    var dateCreated: Long = Constants.NULL_LONG,
    var isDeleted: Boolean = false,

    var maxSeats: Int = Constants.NULL_INT,
    var bookedSeats: Int = Constants.NULL_INT,
    var remainingSeats: Int = Constants.NULL_INT,

    var expensePerPerson: Int = Constants.NULL_INT,
    var totalExpenseGathered: Int = Constants.NULL_INT,

    var bookedUsers: String = Constants.NULL_STRING
) : Parcelable