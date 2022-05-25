package com.example.roadtrippers.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.roadtrippers.R
import com.example.roadtrippers.database.local.UserDatabase
import com.example.roadtrippers.database.remote.callback.DataChangeListener
import com.example.roadtrippers.database.remote.controller.DatabaseController
import com.example.roadtrippers.databinding.FragmentProfileBinding
import com.example.roadtrippers.model.User
import com.example.roadtrippers.ui.activity.HomeActivity
import com.example.roadtrippers.ui.activity.MainActivity
import com.example.roadtrippers.util.navigation.NavigationUtils
import com.example.roadtrippers.util.preference.RememberMePreference
import com.example.roadtrippers.util.toast.ToastUtil
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val rememberMePreference: RememberMePreference by inject()
    private val userDatabase: UserDatabase by inject()
    private val databaseController: DatabaseController by inject()
    private val toastUtil: ToastUtil by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        databaseController.getUserById(
            userDatabase.getCurrUser().id,
            dataChangeListener = object : DataChangeListener {
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
        }
    }

    private fun setListeners() {

        binding.lytPersonalInfo.setOnClickListener {
            NavigationUtils.navigate(
                binding.root,
                R.id.action_profileFragment_to_userInformationFragment
            )
        }

        binding.lytTravellingInstructions.setOnClickListener {
            NavigationUtils.navigate(
                binding.root,
                R.id.action_profileFragment_to_travelInfoFragment
            )
        }

        binding.lytSignOut.setOnClickListener {
            signOutUser()
        }
    }

    private fun signOutUser() {
        userDatabase.deleteCurrUser()
        // Deleting remember me user preference
        rememberMePreference.deleteCurrUserCredentials()
        // Moving user back to login fragment
        findNavController().popBackStack(
            R.id.splashFragment,
            false
        )
        moveToLoginScreen()
    }

    private fun moveToLoginScreen() {
        startActivity(Intent(requireActivity() as HomeActivity, MainActivity::class.java))
        (requireActivity() as HomeActivity).finish()
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }

}