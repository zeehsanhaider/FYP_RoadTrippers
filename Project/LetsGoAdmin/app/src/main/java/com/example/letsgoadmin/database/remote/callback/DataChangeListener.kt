package com.example.letsgoadmin.database.remote.callback

interface DataChangeListener {
    fun onDataChange(list: List<Any>)
    fun onCancel(message: String)
}