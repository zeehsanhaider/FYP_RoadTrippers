package com.example.letsgoadmin.database.local

import android.content.Context
import com.example.letsgoadmin.model.User
import io.paperdb.Paper

class UserDatabase(context: Context) {

    private var user: User = User()
    private val CURR_USER_PAPER_KEY: String = "CURR_USER_PAPER_KEY"

    init {
        Paper.init(context)
        user = Paper.book().read<User>(CURR_USER_PAPER_KEY, user) as User
    }

    fun isCurrUserSaved(): Boolean {
        return user.id != ""
    }

    fun setCurrUser(currUser: User) {
        user = currUser
        Paper.book().write(CURR_USER_PAPER_KEY, user)
    }

    fun getCurrUser(): User {
        return user
    }
}