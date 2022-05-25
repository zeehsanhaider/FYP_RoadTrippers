package com.example.letsgoadmin.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.letsgoadmin.adapter.DefaultTripDataAdapter
import com.example.letsgoadmin.database.remote.callback.DataChangeListener
import com.example.letsgoadmin.database.remote.controller.DatabaseController
import com.example.letsgoadmin.databinding.FragmentTripsBinding
import com.example.letsgoadmin.model.DefaultTripData
import com.example.letsgoadmin.model.DefaultTripTicket
import com.example.letsgoadmin.model.Trip
import com.example.letsgoadmin.model.User
import com.example.letsgoadmin.util.extension.setLinearLayoutManager
import com.example.letsgoadmin.util.toast.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TripsFragment : Fragment() {

    private lateinit var binding: FragmentTripsBinding
    private val databaseController: DatabaseController by inject()
    private val toastUtil: ToastUtil by inject()
    private lateinit var defaultTripDataAdapter: DefaultTripDataAdapter
    private val defaultTripsData = ArrayList<DefaultTripData>()

    private var users: List<User>? = null
    private var trips: List<Trip>? = null
    private var tickets: List<DefaultTripTicket>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripsBinding.inflate(inflater, container, false)
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

        databaseController.getAllTrips(
            object : DataChangeListener {
                override fun onDataChange(list: List<Any>) {
                    trips = list.filterIsInstance<Trip>()
                    Log.e(TAG, "onDataChange: trips ${trips?.size}")
                    updateUi()
                }

                override fun onCancel(message: String) {
                    toastUtil.shortToast(message)
                }
            }
        )

        databaseController.getAllDefaultTripTickets(
            object : DataChangeListener {
                override fun onDataChange(list: List<Any>) {
                    tickets = list.filterIsInstance<DefaultTripTicket>()
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
            trips?.let { tripsList ->
                tickets?.let { ticketsList ->
                    CoroutineScope(Dispatchers.IO).launch {
                        defaultTripsData.clear()
                        for (ticket in ticketsList) {
                            val trip = getTrip(ticket.tripId, tripsList)
                            val user = getUser(ticket.userId, usersList)
                            defaultTripsData.add(
                                DefaultTripData(
                                    ticket = ticket,
                                    user = user,
                                    trip = trip
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

    private suspend fun getTrip(tripId: String, trips: List<Trip>): Trip {
        for (trip in trips) {
            if (trip.id == tripId) {
                return trip
            }
        }
        return Trip()
    }

    private suspend fun getUser(userId: String, users: List<User>): User {
        for (user in users) {
            if (user.id == userId) {
                return user
            }
        }
        return User()
    }

    private fun setDefaultTripsDataRv() {

        defaultTripDataAdapter = DefaultTripDataAdapter(
            requireContext(),
            defaultTripsData = defaultTripsData
        )

        setLinearLayoutManager(
            requireContext(),
            binding.rvDefaultTripsData,
            true
        )

        binding.rvDefaultTripsData.adapter = defaultTripDataAdapter

        defaultTripDataAdapter.setOnItemClickListener { clickedItem, _ ->
            findNavController().navigate(
                TripsFragmentDirections.actionTripsFragmentToDefaultTripDetailsFragment(
                    ticket = clickedItem.ticket,
                    trip = clickedItem.trip,
                    user = clickedItem.user
                )
            )
        }
    }

    companion object {
        private const val TAG = "TripsFragment"
    }

}