package com.example.letsgoadmin.model

import android.os.Parcelable
import com.example.letsgoadmin.util.constant.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    var id: String = Constants.NULL_STRING,
    var title: String = Constants.NULL_STRING,
    var description: String = Constants.NULL_STRING,
    var imageUrls: String = Constants.NULL_STRING,
    var dateCreated: Long = Constants.NULL_LONG,
    var isDeleted: Boolean = false,
    var expensePerPerson: Int = Constants.NULL_INT
) : Parcelable