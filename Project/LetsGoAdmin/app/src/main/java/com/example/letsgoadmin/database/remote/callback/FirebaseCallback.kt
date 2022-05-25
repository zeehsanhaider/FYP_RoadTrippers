package com.example.letsgoadmin.database.remote.callback

interface FirebaseCallback {
    fun onSuccess(message: String)
    fun onNoSuccess(message: String)
    fun onFailure(message: String)
    fun onCancel(message: String)
}