package com.example.roadtrippers.model

import com.example.roadtrippers.util.constant.Constants.NULL_INT
import com.example.roadtrippers.util.constant.Constants.NULL_STRING
import java.util.*

data class TripTicket(

    var id: String = NULL_STRING,
    var tripId: String = NULL_STRING,
    var userId: String = NULL_STRING,
    var vehicleId: String = NULL_STRING,

    // After wards we will add a 1,2,3 days and week duration trip
    // User can search trips by their duration
    var tripDays: Int = NULL_INT,
    var departureDate: String = NULL_STRING,
    var departureTime: String = NULL_STRING,

    var dateCreated: Long = Date().time,
    var isDeleted: Boolean = false,

    var persons: Int = NULL_INT,
    var expense: Int = NULL_INT,

    var contact: String = NULL_STRING

)