package com.example.roadtrippers.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roadtrippers.adapter.TripPicsAdapter
import com.example.roadtrippers.database.local.UserDatabase
import com.example.roadtrippers.database.remote.callback.DataChangeListener
import com.example.roadtrippers.database.remote.controller.DatabaseController
import com.example.roadtrippers.databinding.FragmentPlaceDetailsBinding
import com.example.roadtrippers.model.Place
import com.example.roadtrippers.util.extension.gone
import com.example.roadtrippers.util.extension.visible
import com.example.roadtrippers.util.toast.ToastUtil
import org.koin.android.ext.android.inject

class PlaceDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPlaceDetailsBinding
    private val args: PlaceDetailsFragmentArgs by navArgs()
    private val databaseController: DatabaseController by inject()
    private val toastUtil: ToastUtil by inject()
    private lateinit var tripPicsAdapter: TripPicsAdapter
    private val userDatabase: UserDatabase by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        Log.e(TAG, "initComponents: ${args.selectedPlace}")
        fetchCurrPlaceData(args.selectedPlace.id)
        checkIfAlreadyBooked()
    }

    private fun checkIfAlreadyBooked() {
        databaseController.isPlaceAlreadyBooked(
            args.selectedPlace.id,
            userDatabase.getCurrUser().id,
            object : DataChangeListener {
                override fun onDataChange(list: List<Any>) {
                    val isAlreadyBooked = list[0] as Boolean
                    Log.e(
                        TAG,
                        "onDataChange: userId: ${userDatabase.getCurrUser().id} / isAlreadyBooked: $isAlreadyBooked"
                    )
                    if (isAlreadyBooked) {
                        binding.txAlreadyBooked.visible()
                        binding.btnBookTrip.gone()
                    }
                }

                override fun onCancel(message: String) {}
            }
        )
    }

    private fun fetchCurrPlaceData(placeId: String) {
        databaseController.getPlaceById(
            placeId,
            object : DataChangeListener {
                override fun onDataChange(list: List<Any>) {
                    if (list.isNotEmpty()) {
                        val currPlace = list[0] as Place
                        setPlaceData(currPlace)
                    } else {
                        Log.e(TAG, "onDataChange: trip by id: $placeId does not exists.")
                    }
                }

                override fun onCancel(message: String) {
                    toastUtil.shortToast(message)
                }

            })
    }

    @SuppressLint("SetTextI18n")
    private fun setPlaceData(currPlace: Place) {
        tripPicsAdapter = TripPicsAdapter(requireContext(), currPlace.imageUrls.split(','))
        setPlacePicsAdapter()
        with(binding) {
            txTripTitle.text = currPlace.title
            txTripDescription.text = currPlace.description
            txTripPerPersonFair.text = "${currPlace.expensePerPerson} Rs"
        }
        setListeners()
    }

    private fun setPlacePicsAdapter() {
        with(binding.rvTripPics) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = tripPicsAdapter
        }
    }

    private fun setListeners() {
        tripPicsAdapter.setOnItemClickListener { _, _ -> }
        binding.btnBookTrip.setOnClickListener {
            findNavController().navigate(
                PlaceDetailsFragmentDirections.actionPlaceDetailsFragmentToBookTripFragment(
                    selectedPlace = args.selectedPlace
                )
            )
        }
    }

    companion object {
        private const val TAG = "PlaceDetailsFragment"
    }
}