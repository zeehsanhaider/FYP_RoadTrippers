package com.example.letsgoadmin.model

import com.example.letsgoadmin.util.constant.Constants

data class Vehicle(
    var id: String = Constants.NULL_STRING,

    var name: String = Constants.NULL_STRING,
    var dateCreated: Long = Constants.NULL_LONG,
    var imgUrl: String = Constants.NULL_STRING,

    var seats: Int = Constants.NULL_INT,
    var mileage: Int = Constants.NULL_INT,
    var expenseFactor: Double = Constants.NULL_DOUBLE
)