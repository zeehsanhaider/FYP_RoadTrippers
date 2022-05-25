package com.example.letsgoadmin.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.letsgoadmin.adapter.CustomTripDataAdapter
import com.example.letsgoadmin.database.remote.callback.DataChangeListener
import com.example.letsgoadmin.database.remote.controller.DatabaseController
import com.example.letsgoadmin.databinding.FragmentUsersBinding
import com.example.letsgoadmin.model.CustomTripData
import com.example.letsgoadmin.model.CustomTripTicket
import com.example.letsgoadmin.model.Place
import com.example.letsgoadmin.model.User
import com.example.letsgoadmin.util.extension.setLinearLayoutManager
import com.example.letsgoadmin.util.toast.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class UsersFragment : Fragment() {

    private lateinit var binding: FragmentUsersBinding
    private val databaseController: DatabaseController by inject()
    private val toastUtil: ToastUtil by inject()
    private val customTripsData = ArrayList<CustomTripData>()
    private lateinit var customTripDataAdapter: CustomTripDataAdapter

    private var users: List<User>? = null
    private var places: List<Place>? = null
    private var tickets: List<CustomTripTicket>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        fetchData()
    }

    private fun fetchData() {
        databaseController.getAllUsers(
            object : DataChangeListener {
                override fun onDataChange(list: List<Any>) {
                    users = list.filterIsInstance<User>()
                    Log.e(TAG, "onDataChange: users ${users?.size}")
                    updateUi()
                }

                override fun onCancel(message: String) {
                    toastUtil.shortToast(message)
                }
            }
        )

        databaseController.getAllPlaces(
            object : DataChangeListener {
                override fun onDataChange(list: List<Any>) {
                    places = list.filterIsInstance<Place>()
                    Log.e(TAG, "onDataChange: trips ${places?.size}")
                    updateUi()
                }

                override fun onCancel(message: String) {
                    toastUtil.shortToast(message)
                }
            }
        )

        databaseController.getAllCustomTripTickets(
            object : DataChangeListener {
                override fun onDataChange(list: List<Any>) {
                    tickets = list.filterIsInstance<CustomTripTicket>()
                    Log.e(TAG, "onDataChange: tickets ${tickets?.size}")
                    updateUi()
                }

                override fun onCancel(message: String) {
                    toastUtil.shortToast(message)
                }
            }
        )
    }

    private fun updateUi() {
        users?.let { usersList ->
            places?.let { tripsList ->
                tickets?.let { ticketsList ->
                    CoroutineScope(Dispatchers.IO).launch {
                        customTripsData.clear()
                        for (ticket in ticketsList) {
                            val place = getPlace(ticket.placeId, tripsList)
                            val user = getUser(ticket.userId, usersList)
                            customTripsData.add(
                                CustomTripData(
                                    ticket = ticket,
                                    place = place,
                                    user = user
                                )
                            )
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            setDefaultTripsDataRv()
                        }
                    }
                }
            }
        }
    }

    private fun setDefaultTripsDataRv() {

        customTripDataAdapter = CustomTripDataAdapter(
            requireContext(),
            customTripsData
        )

        setLinearLayoutManager(
            requireContext(),
            binding.rvCustomTripsData,
            true
        )

        binding.rvCustomTripsData.adapter = customTripDataAdapter

        customTripDataAdapter.setOnItemClickListener { clickedItem, _ ->
            findNavController().navigate(
                UsersFragmentDirections.actionUsersFragmentToCustomTripDetailsFragment(
                    ticket = clickedItem.ticket,
                    place = clickedItem.place,
                    user = clickedItem.user
                )
            )
        }
    }

    private fun getPlace(placeId: String, places: List<Place>): Place {
        for (place in places) {
            if (place.id == placeId) {
                return place
            }
        }
        return Place()
    }

    private fun getUser(userId: String, users: List<User>): User {
        for (user in users) {
            if (user.id == userId) {
                return user
            }
        }
        return User()
    }

    companion object {
        private const val TAG = "UsersFragment"
    }

}