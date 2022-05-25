package com.example.roadtrippers.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.roadtrippers.database.local.UserDatabase
import com.example.roadtrippers.database.remote.callback.DataChangeListener
import com.example.roadtrippers.database.remote.controller.DatabaseController
import com.example.roadtrippers.databinding.FragmentRegisterBinding
import com.example.roadtrippers.model.User
import com.example.roadtrippers.ui.activity.HomeActivity
import com.example.roadtrippers.ui.activity.MainActivity
import com.example.roadtrippers.util.constant.Constants
import com.example.roadtrippers.util.preference.RememberMePreference
import com.example.roadtrippers.util.toast.ToastUtil
import com.example.roadtrippers.database.remote.callback.FirebaseCallback
import org.koin.android.ext.android.inject
import java.util.*
import java.util.regex.Pattern

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val toastUtil by inject<ToastUtil>()
    private val rememberMePreference: RememberMePreference by inject()
    private val databaseController: DatabaseController by inject()
    private val userDatabase: UserDatabase by inject()

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        setListeners()
    }

    private fun setListeners() {
        // Signup btn click listener
        binding.btnLogin.setOnClickListener {
            validateCredentials(
                binding.edtName.text.toString(),
                binding.edtEmail.text.toString(),
                binding.edtPassword.text.toString()
            )
        }
        // Remember Me Checkbox listener
        binding.cbRememberMe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rememberMePreference.setRememberMeToChecked()
            } else {
                rememberMePreference.setRememberMeToUnChecked()
            }
        }
    }

    private fun validateCredentials(name: String, email: String, password: String) {
        if (email.isEmpty()) {
            toastUtil.longToast("Please provide an email address.")
            return
        }
        if (password.isEmpty()) {
            toastUtil.longToast("Please provide a password.")
            return
        }
        val validEmail = isValidEmail(email)
        Log.e(TAG, "validateCredentials: { $email, $validEmail }")
        if (validEmail) {
            databaseController.doesEmailAlreadyExists(
                email,
                object : DataChangeListener {
                    override fun onDataChange(list: List<Any>) {
                        val emailExists = list[0] as Boolean
                        if (emailExists) {
                            toastUtil.longToast("Email $email is already registered.")
                            return
                        } else {
                            val newUser = getNewUser(name, email, password)
                            if (newUser.id == Constants.NO_ID_ASSIGNED_USER) {
                                toastUtil.longToast("Could not make account due to some technical issues.")
                                return
                            } else {
                                saveCurrUser(newUser)
                                databaseController.addUser(
                                    newUser,
                                    object : FirebaseCallback {
                                        override fun onSuccess(message: String) {
                                            implementRememberMe(email, password)
                                            toastUtil.longToast("Account created successfully!")
                                            moveToHome()
                                        }

                                        override fun onNoSuccess(message: String) {
                                            toastUtil.longToast("Account creation error!")
                                        }

                                        override fun onFailure(message: String) {
                                            toastUtil.longToast("Account creation cancelled!")
                                        }

                                        override fun onCancel(message: String) {
                                            toastUtil.longToast("Account creation failed!")
                                        }
                                    }
                                )
                            }
                        }
                    }

                    override fun onCancel(message: String) {}
                }
            )
        } else {
            toastUtil.longToast("Invalid email address provided.")
            return
        }
    }

    private fun implementRememberMe(email: String, password: String) {
        if (rememberMePreference.isRememberMeChecked()) {
            if (!rememberMePreference.isCurrUserCredentialsSaved()) {
                rememberMePreference.setCurrUserCredentials(
                    email,
                    password
                )
            }
        }
    }

    private fun moveToHome() {
        startActivity(Intent(requireActivity() as MainActivity, HomeActivity::class.java))
        (requireActivity() as MainActivity).finish()
    }

    private fun isValidEmail(str: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }

    private fun getNewUser(name: String, email: String, password: String): User {
        val userId = databaseController.getUniqueUserId()
        val newUser = if (userId != null) {
            User(
                id = userId,
                name = name,
                email = email,
                password = password,
                isAdmin = false,
                dateCreated = Date().time
            )
        } else {
            User(Constants.NO_ID_ASSIGNED_USER)
        }
        return newUser
    }

    private fun saveCurrUser(user: User) {
        userDatabase.setCurrUser(user)
    }

    companion object {
        private const val TAG = "RegisterFragment"
    }
}