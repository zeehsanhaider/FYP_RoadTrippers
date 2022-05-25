package com.example.letsgoadmin.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.letsgoadmin.R
import com.example.letsgoadmin.database.local.UserDatabase
import com.example.letsgoadmin.database.remote.callback.DataChangeListener
import com.example.letsgoadmin.database.remote.controller.DatabaseController
import com.example.letsgoadmin.databinding.FragmentLoginBinding
import com.example.letsgoadmin.model.User
import com.example.letsgoadmin.ui.activity.HomeActivity
import com.example.letsgoadmin.ui.activity.MainActivity
import com.example.letsgoadmin.util.navigation.NavigationUtils
import com.example.letsgoadmin.util.preference.RememberMePreference
import com.example.letsgoadmin.util.toast.ToastUtil
import org.koin.android.ext.android.inject
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
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

        binding.btnLogin.setOnClickListener {
            validateCredentials(
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

        binding.lytSignUpForAccount.setOnClickListener {
            NavigationUtils.navigate(
                binding.root,
                R.id.action_loginFragment_to_registerFragment
            )
        }

    }

    private fun validateCredentials(email: String, password: String) {
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
            databaseController.loginUser(
                email,
                password,
                true,
                object : DataChangeListener {
                    override fun onDataChange(list: List<Any>) {
                        val loginResponse = list[0] as Boolean
                        if (loginResponse) {
                            // Login Success
                            proceedLogin(email, password)
                        } else {
                            toastUtil.longToast("Wrong credentials entered!")
                        }
                    }

                    override fun onCancel(message: String) {
                        toastUtil.longToast("Something went wrong! Please check your internet connection.")
                    }
                }
            )
        } else {
            toastUtil.longToast("Invalid email address provided.")
            return
        }
    }

    private fun proceedLogin(email: String, password: String) {
        databaseController.getUserByEmail(
            email = email,
            dataChangeListener = object : DataChangeListener {
                override fun onDataChange(list: List<Any>) {
                    val incomingUser = list[0] as User
                    saveCurrUser(incomingUser)
                    implementRememberMe(email, password)
                    toastUtil.longToast("Logged in successfully!")
                    moveToHome()
                }

                override fun onCancel(message: String) {
                    Log.e(TAG, "onCancel: $message")
                }
            }
        )


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
        startActivity(
            Intent(requireActivity() as MainActivity, HomeActivity::class.java)
        )
        (requireActivity() as MainActivity).finish()
    }

    private fun isValidEmail(str: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }

    private fun saveCurrUser(user: User) {
        if (!userDatabase.isCurrUserSaved()) {
            userDatabase.setCurrUser(user)
        }
    }

    companion object {
        private const val TAG = "LoginFragment"
    }

}