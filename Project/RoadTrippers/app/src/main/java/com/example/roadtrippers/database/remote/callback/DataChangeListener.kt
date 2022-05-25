package com.example.roadtrippers.database.remote.callback

interface DataChangeListener {
    fun onDataChange(list: List<Any>)
    fun onCancel(message: String)
}