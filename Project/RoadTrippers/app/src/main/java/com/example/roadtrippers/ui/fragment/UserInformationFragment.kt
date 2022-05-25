package com.example.roadtrippers.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.roadtrippers.database.local.UserDatabase
import com.example.roadtrippers.database.remote.callback.DataChangeListener
import com.example.roadtrippers.database.remote.controller.DatabaseController
import com.example.roadtrippers.databinding.FragmentUserInfomationBinding
import com.example.roadtrippers.model.User
import com.example.roadtrippers.util.preference.RememberMePreference
import com.example.roadtrippers.util.toast.ToastUtil
import com.example.roadtrippers.database.remote.callback.FirebaseCallback
import org.koin.android.ext.android.inject

class UserInformationFragment : Fragment() {

    private lateinit var binding: FragmentUserInfomationBinding
    private val userDatabase: UserDatabase by inject()
    private val databaseController: DatabaseController by inject()
    private val toastUtil: ToastUtil by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfomationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        databaseController.getUserById(
            userDatabase.getCurrUser().id,
            object : DataChangeListener {
                override fun onDataChange(list: List<Any>) {
                    setUserData(list[0] as User)
                }

                override fun onCancel(message: String) {
                    Log.e(TAG, "onCancel: $message")
                }
            }
        )
        setListeners()
    }

    private fun setUserData(user: User) {
        with(binding) {
            txEmpName.text = user.name
            txEmpEmail.text = user.email
            txEmpProfile.text = "${user.name[0]}"

            edtEmpName.setText(user.name)
            edtEmpEmail.setText(user.email)
        }
    }

    private fun setListeners() {
        binding.btnUpdatePassword.setOnClickListener {
            val currPass = binding.edtOldPass.text.toString()
            val newPass = binding.edtNewPassword.text.toString()
            // Validations
            if (currPass.isEmpty()) {
                toastUtil.shortToast("Please provide your current password")
                return@setOnClickListener
            }
            if (newPass.isEmpty()) {
                toastUtil.shortToast("Please provide your new password")
                return@setOnClickListener
            }
            if (currPass != userDatabase.getCurrUser().password) {
                toastUtil.shortToast("Wrong current password entered!")
                return@setOnClickListener
            }
            // Updating password in local db
            val updatedPassUser = userDatabase.getCurrUser()
            updatedPassUser.password = newPass
            userDatabase.setCurrUser(updatedPassUser)
            // Updating password on firebase db
            databaseController.addUser(
                user = updatedPassUser,
                callback = object : FirebaseCallback {
                    override fun onSuccess(message: String) {
                        toastUtil.shortToast("Password updated successfully.")
                    }

                    override fun onNoSuccess(message: String) {
                        toastUtil.shortToast("Failure while updating password.")
                    }

                    override fun onFailure(message: String) {
                        toastUtil.shortToast("Failure while updating password.")
                    }

                    override fun onCancel(message: String) {
                        toastUtil.shortToast("Failure while updating password.")
                    }
                }
            )
        }
    }

    companion object {
        private const val TAG = "UserInformationFragment"
    }
}