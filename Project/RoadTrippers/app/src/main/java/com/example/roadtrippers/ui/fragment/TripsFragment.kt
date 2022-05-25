package com.example.roadtrippers.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roadtrippers.adapter.TripsAdapter
import com.example.roadtrippers.database.remote.callback.DataChangeListener
import com.example.roadtrippers.database.remote.controller.DatabaseController
import com.example.roadtrippers.databinding.FragmentTripsBinding
import com.example.roadtrippers.model.Trip
import com.example.roadtrippers.util.toast.ToastUtil
import org.koin.android.ext.android.inject

class TripsFragment : Fragment() {

    private lateinit var binding: FragmentTripsBinding
    private lateinit var tripsAdapter: TripsAdapter
    private val databaseController: DatabaseController by inject()
    private val toastUtil: ToastUtil by inject()

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
        /**
        val vehicleId = databaseController.getUniqueVehicleId()
        vehicleId?.let {
        databaseController.addVehicle(
        Vehicle(
        id = it,
        name = "Suzuki Wagon R",
        mileage = 22,
        seats = 4,
        imgUrl = "https://firebasestorage.googleapis.com/v0/b/roadtrippers-62faa.appspot.com/o/Vehicle_Images%2Fic_wagon_r.jpg?alt=media&token=8d449924-0409-45d4-8d76-a86e2ed0bd02",
        dateCreated = Date().time,
        expenseFactor = 1.2
        ),
        callback = object : FirebaseCallback {
        override fun onSuccess(message: String) {
        Log.e(TAG, "onSuccess: $message")
        }

        override fun onNoSuccess(message: String) {
        Log.e(TAG, "onNoSuccess: $message")
        }

        override fun onFailure(message: String) {
        Log.e(TAG, "onFailure: $message")
        }

        override fun onCancel(message: String) {
        Log.e(TAG, "onCancel: $message")
        }
        }
        )
        }
         **/

        setTripsRv()
        setListeners()
        fetchAllTrips()
    }

    private fun fetchAllTrips() {

        databaseController.getAllTrips(object : DataChangeListener {
            override fun onDataChange(list: List<Any>) {
                val trips: List<Trip> = list.filterIsInstance<Trip>()
                tripsAdapter.differ.submitList(trips)
            }

            override fun onCancel(message: String) {
                toastUtil.shortToast(message)
            }
        })
    }

    private fun setTripsRv() {
        with(binding.rvTrips) {
            tripsAdapter = TripsAdapter(requireContext())
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = tripsAdapter
        }
    }

    private fun setListeners() {
        tripsAdapter.setOnItemClickListener { tripItem, _ ->
            findNavController().navigate(
                TripsFragmentDirections.actionTripsFragmentToTripDetailsFragment(
                    tripId = tripItem.id
                )
            )
        }
    }

    companion object {
        private const val TAG = "TripsFragment"
    }

}