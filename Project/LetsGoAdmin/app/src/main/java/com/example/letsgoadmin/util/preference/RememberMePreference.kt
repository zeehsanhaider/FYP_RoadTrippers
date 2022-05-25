package com.example.letsgoadmin.util.preference

import android.content.Context
import android.content.SharedPreferences
import com.example.letsgoadmin.util.constant.Constants.NULL_STRING

class RememberMePreference(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("My_Shared_Preferences", Context.MODE_PRIVATE)

    private val CURR_USER_EMAIL_KEY = "Current User Email"
    private val CURR_USER_PASS_KEY = "Current User Password"
    private val REMEMBER_ME_FLAG_KEY = "Current User Remember Me Preference"

    fun isRememberMeChecked(): Boolean {
        return sharedPreferences.getBoolean(REMEMBER_ME_FLAG_KEY, false)
    }

    fun setRememberMeToChecked() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(REMEMBER_ME_FLAG_KEY, true)
        editor.apply()
    }

    fun setRememberMeToUnChecked() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(REMEMBER_ME_FLAG_KEY, false)
        editor.apply()
    }

    fun setCurrUserCredentials(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(CURR_USER_EMAIL_KEY, email)
        editor.putString(CURR_USER_PASS_KEY, password)
        editor.apply()
    }

    fun getCurrUserCredentials(): List<String> {
        val email = sharedPreferences.getString(CURR_USER_EMAIL_KEY, NULL_STRING) ?: NULL_STRING
        val password = sharedPreferences.getString(CURR_USER_PASS_KEY, NULL_STRING) ?: NULL_STRING
        return listOf(email, password)
    }

    fun isCurrUserCredentialsSaved(): Boolean {
        val email = sharedPreferences.getString(CURR_USER_EMAIL_KEY, NULL_STRING) ?: NULL_STRING
        val password = sharedPreferences.getString(CURR_USER_PASS_KEY, NULL_STRING) ?: NULL_STRING
        return (email != NULL_STRING && password != NULL_STRING)
    }

    fun deleteCurrUserCredentials() {
        setRememberMeToUnChecked()
        val editor = sharedPreferences.edit()
        editor.putString(CURR_USER_EMAIL_KEY, NULL_STRING)
        editor.putString(CURR_USER_PASS_KEY, NULL_STRING)
        editor.apply()
    }

}