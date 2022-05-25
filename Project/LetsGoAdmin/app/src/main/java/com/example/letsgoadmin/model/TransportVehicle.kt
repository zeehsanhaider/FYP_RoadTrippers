package com.example.letsgoadmin.model

import com.example.letsgoadmin.util.constant.Constants.NULL_DOUBLE
import com.example.letsgoadmin.util.constant.Constants.NULL_INT
import com.example.letsgoadmin.util.constant.Constants.NULL_LONG
import com.example.letsgoadmin.util.constant.Constants.NULL_STRING
import java.util.*

data class TransportVehicle(

    var id: String = NULL_STRING,

    var name: String = NULL_STRING,

    var perLitreMileage: Int = NULL_INT,

    var imgUrl: String = NULL_STRING,

    var seats: Int = NULL_INT,

    var expenseIncrementFactor: Double = NULL_DOUBLE,

    var isDeleted: Boolean = false,

    var dateCreated: Long = NULL_LONG

)