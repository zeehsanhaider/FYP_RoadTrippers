package com.example.roadtrippers.database.local

import android.content.Context
import android.util.Log
import com.example.roadtrippers.model.User
import com.example.roadtrippers.util.constant.Constants
import io.paperdb.Paper

class UserDatabase(context: Context) {

    private var user: User = User()
    private val CURR_USER_PAPER_KEY: String = "CURR_USER_PAPER_KEY"

    init {
        Log.e(TAG, "Init()")
        Paper.init(context)
        user = Paper.book().read<User>(CURR_USER_PAPER_KEY, user) as User
    }

    fun isCurrUserSaved(): Boolean {
        return user.id != Constants.NULL_STRING
    }

    fun setCurrUser(currUser: User) {
        user = currUser
        Paper.book().write(CURR_USER_PAPER_KEY, user)
        Log.e(TAG, "setCurrUser: user saved: $user")
    }

    fun getCurrUser(): User {
        user = Paper.book().read<User>(CURR_USER_PAPER_KEY, user) as User
        return user
    }

    fun deleteCurrUser() {
        Paper.book().destroy()
    }

    companion object {
        private const val TAG = "UserDatabase"
    }
}