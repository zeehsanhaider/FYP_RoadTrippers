package com.example.roadtrippers.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.roadtrippers.R
import com.example.roadtrippers.adapter.MyCustomTripAdapter
import com.example.roadtrippers.adapter.MyTripsAdapter
import com.example.roadtrippers.database.local.UserDatabase
import com.example.roadtrippers.database.remote.callback.DataChangeListener
import com.example.roadtrippers.database.remote.controller.DatabaseController
import com.example.roadtrippers.databinding.FragmentMyTripsBinding
import com.example.roadtrippers.model.CustomTripTicket
import com.example.roadtrippers.model.DefaultTripTicket
import com.example.roadtrippers.model.Place
import com.example.roadtrippers.model.Trip
import com.example.roadtrippers.util.extension.gone
import com.example.roadtrippers.util.extension.setLinearLayoutManager
import com.example.roadtrippers.util.extension.visible
import com.example.roadtrippers.util.toast.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MyTripsFragment : Fragment() {

    private lateinit var binding: FragmentMyTripsBinding

    private lateinit var myTripsAdapter: MyTripsAdapter
    private lateinit var myCustomTripAdapter: MyCustomTripAdapter

    private val databaseController: DatabaseController by inject()
    private val toastUtil: ToastUtil by inject()
    private val userDatabase: UserDatabase by inject()

    private val defaultTrips = ArrayList<Trip>()
    private val defaultTripsTickets = ArrayList<DefaultTripTicket>()

    private val customTrips = ArrayList<Place>()
    private val customTripsTickets = ArrayList<CustomTripTicket>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyTripsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        getUserDefaultTrips()
        getUserCustomTrips()
        binding.toggleButtonGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (checkedId == R.id.btn_default_trips && isChecked) {
                Log.e(TAG, "initComponents: Default trips clicked")
                with(binding) {
                    rvMyPlaceTours.gone()
                    rvMyTrips.visible()
                }
            }
            if (checkedId == R.id.btn_custom_trips && isChecked) {
                Log.e(TAG, "initComponents: Custom trips clicked")
                with(binding) {
                    rvMyTrips.gone()
                    rvMyPlaceTours.visible()
                }
            }
        }
    }

    private fun getUserDefaultTrips() {
        databaseController.getAllTrips(object : DataChangeListener {
            override fun onDataChange(list: List<Any>) {

                val incomingTrips: List<Trip> = list.filterIsInstance<Trip>()

                databaseController.getAllDefaultTripTickets(object : DataChangeListener {
                    override fun onDataChange(list: List<Any>) {

                        val incomingTripTickets: List<DefaultTripTicket> =
                            list.filterIsInstance<DefaultTripTicket>()

                        Log.e(TAG, "onDataChange: _defaultTrips: ${incomingTrips.size}")
                        Log.e(
                            TAG,
                            "onDataChange: _defaultTripsTickets: ${incomingTripTickets.size}"
                        )

                        Log.e(
                            TAG,
                            "onDataChange: current user id: ${userDatabase.getCurrUser().id}"
                        )

                        CoroutineScope(Dispatchers.IO).launch {

                            defaultTrips.clear()
                            defaultTripsTickets.clear()

                            for (tripTicket in incomingTripTickets) {
                                if (tripTicket.userId == userDatabase.getCurrUser().id) {
                                    for (trip in incomingTrips) {
                                        if (tripTicket.tripId == trip.id) {
                                            defaultTrips.add(trip)
                                            defaultTripsTickets.add(tripTicket)
                                        }
                                    }
                                }
                            }

                            CoroutineScope(Dispatchers.Main).launch {
                                setMyTripsRv()
                            }
                        }
                        Log.e(
                            TAG,
                            "onDataChange: ${defaultTrips.size}, ${defaultTripsTickets.size}"
                        )
                    }

                    override fun onCancel(message: String) {
                        toastUtil.shortToast(message)
                    }
                })
            }

            override fun onCancel(message: String) {
                toastUtil.shortToast(message)
            }
        })
    }

    private fun getUserCustomTrips() {
        databaseController.getAllPlaces(object : DataChangeListener {
            override fun onDataChange(list: List<Any>) {

                val incomingPlaces: List<Place> = list.filterIsInstance<Place>()

                databaseController.getAllCustomTripTickets(object : DataChangeListener {
                    override fun onDataChange(list: List<Any>) {

                        val incomingCustomTripTickets: List<CustomTripTicket> =
                            list.filterIsInstance<CustomTripTicket>()

                        Log.e(TAG, "onDataChange: incomingPlaces: ${incomingPlaces.size}")
                        Log.e(
                            TAG,
                            "onDataChange: incomingCustomTripTickets: ${incomingCustomTripTickets.size}"
                        )

                        CoroutineScope(Dispatchers.IO).launch {

                            customTrips.clear()
                            customTripsTickets.clear()

                            for (customTripTicket in incomingCustomTripTickets) {
                                if (customTripTicket.userId == userDatabase.getCurrUser().id) {
                                    for (place in incomingPlaces) {
                                        if (customTripTicket.placeId == place.id) {
                                            customTrips.add(place)
                                            customTripsTickets.add(customTripTicket)
                                        }
                                    }
                                }
                            }

                            CoroutineScope(Dispatchers.Main).launch {
                                setMyCustomTripsRv()
                            }
                        }
                        Log.e(
                            TAG,
                            "onDataChange: Custom ${customTrips.size}, ${customTripsTickets.size}"
                        )
                    }

                    override fun onCancel(message: String) {
                        toastUtil.shortToast(message)
                    }
                })
            }

            override fun onCancel(message: String) {
                toastUtil.shortToast(message)
            }
        })

    }

    private fun setMyTripsRv() {
        setLinearLayoutManager(
            requireContext(),
            binding.rvMyTrips,
            true
        )
        myTripsAdapter = MyTripsAdapter(
            requireContext(),
            trips = defaultTrips,
            tripTickets = defaultTripsTickets
        )
        binding.rvMyTrips.adapter = myTripsAdapter
        myTripsAdapter.setOnItemClickListener { tripItem, _, _ ->
            findNavController().navigate(
                MyTripsFragmentDirections.actionMyTripsFragmentToTripDetailsFragment(
                    tripId = tripItem.id
                )
            )
        }
    }

    private fun setMyCustomTripsRv() {
        requireContext().let {
            setLinearLayoutManager(
                it,
                binding.rvMyPlaceTours,
                true
            )
            myCustomTripAdapter = MyCustomTripAdapter(
                it,
                places = customTrips,
                customTripTickets = customTripsTickets
            )
            binding.rvMyPlaceTours.adapter = myCustomTripAdapter

            myCustomTripAdapter.setOnItemClickListener { place, _, _ ->
                findNavController().navigate(
                    MyTripsFragmentDirections.actionMyTripsFragmentToPlaceDetailsFragment(
                        selectedPlace = place
                    )
                )
            }
        }
    }

    companion object {
        private const val TAG = "MyTripsFragment"
    }

}