package com.example.roadtrippers.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roadtrippers.adapter.TripPicsAdapter
import com.example.roadtrippers.database.local.UserDatabase
import com.example.roadtrippers.database.remote.controller.DatabaseController
import com.example.roadtrippers.databinding.FragmentTripDetailsBinding
import com.example.roadtrippers.model.DefaultTripTicket
import com.example.roadtrippers.model.Trip
import com.example.roadtrippers.ui.dialog.TripBookedSuccessDialog
import com.example.roadtrippers.ui.dialog.TripBookingInfoDialog
import com.example.roadtrippers.util.constant.Constants
import com.example.roadtrippers.util.toast.ToastUtil
import com.example.roadtrippers.database.remote.callback.DataChangeListener
import com.example.roadtrippers.database.remote.callback.FirebaseCallback
import com.example.roadtrippers.util.extension.gone
import com.example.roadtrippers.util.extension.visible
import org.koin.android.ext.android.inject
import java.util.*

class TripDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTripDetailsBinding
    private lateinit var tripPicsAdapter: TripPicsAdapter
    private val args: TripDetailsFragmentArgs by navArgs()
    private val databaseController: DatabaseController by inject()
    private val toastUtil: ToastUtil by inject()
    private val userDatabase: UserDatabase by inject()
    private lateinit var tripBookingInfoDialog: TripBookingInfoDialog
    private lateinit var tripBookedSuccessDialog: TripBookedSuccessDialog
    private lateinit var selectedTrip: Trip

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        if (args.tripId != Constants.NULL_STRING) {
            fetchCurrTripData(args.tripId)
            checkIfAlreadyBooked()
        } else {
            Log.e(TAG, "initComponents: Incoming curr trip id is null")
        }
    }

    private fun checkIfAlreadyBooked() {
        databaseController.isTripAlreadyBooked(
            args.tripId,
            userDatabase.getCurrUser().id,
            object : DataChangeListener {
                override fun onDataChange(list: List<Any>) {
                    val isAlreadyBooked = list[0] as Boolean
                    if(isAlreadyBooked) {
                        binding.txAlreadyBooked.visible()
                        binding.btnBookTrip.gone()
                    }
                }

                override fun onCancel(message: String) {}
            }
        )
    }

    private fun fetchCurrTripData(tripId: String) {
        databaseController.getTripById(
            tripId, object : DataChangeListener {
                override fun onDataChange(list: List<Any>) {
                    if (list.isNotEmpty()) {
                        val currTrip = list[0] as Trip
                        selectedTrip = currTrip
                        setTripData(currTrip)
                    } else {
                        Log.e(TAG, "onDataChange: trip by id: $tripId does not exists.")
                    }
                }

                override fun onCancel(message: String) {
                    toastUtil.shortToast(message)
                }

            })
    }

    @SuppressLint("SetTextI18n")
    private fun setTripData(currTrip: Trip) {
        tripPicsAdapter = TripPicsAdapter(requireContext(), currTrip.imageUrls.split(','))
        setTripPicsAdapter()
        with(binding) {
            txTripTitle.text = currTrip.title
            txTripDescription.text = currTrip.description

            txDepartureDate.text = currTrip.departureDate
            txDepartureTime.text = currTrip.departureTime

            txTripPerPersonFair.text = "${currTrip.expensePerPerson} Rs"
            txTripDays.text = "${currTrip.tripDays} Days"
            txSeatsAvailable.text = "${currTrip.maxSeats - currTrip.bookedSeats} Seats"
        }
        setListeners()
    }

    private fun setTripPicsAdapter() {
        with(binding.rvTripPics) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = tripPicsAdapter
        }
    }

    private fun setListeners() {
        tripPicsAdapter.setOnItemClickListener { _, _ -> }
        binding.btnBookTrip.setOnClickListener {
//            findNavController().navigate(
//                TripDetailsFragmentDirections.actionTripDetailsFragmentToBookTripFragment(
//                    selectedTrip = selectedTrip
//                )
//            )
            bookThisTrip()
        }
    }

    private fun bookThisTrip() {
        tripBookingInfoDialog = TripBookingInfoDialog(
            context = requireContext(),
            callback = object : TripBookingInfoDialog.DialogCallback {
                override fun onDialogDismiss(noOfPersons: Int, contactNo: String) {
                    bookUserForTrip(noOfPersons, contactNo)
                }
            }
        )
        tripBookingInfoDialog.showDialog()
    }

    private fun bookUserForTrip(persons: Int, contactNumber: String) {
        val ticketId = databaseController.getUniqueTripTicketId()
        ticketId?.let {
            val defaultTripTicket = DefaultTripTicket(
                id = it,
                userId = userDatabase.getCurrUser().id,
                tripId = selectedTrip.id,
                totalExpense = selectedTrip.expensePerPerson * persons,
                contact = contactNumber,
                persons = persons,
                dateCreated = Date().time
            )
            databaseController.addDefaultTripTicket(
                defaultTripTicket,
                object : FirebaseCallback {
                    override fun onSuccess(message: String) {
                        tripBookedSuccessDialog = TripBookedSuccessDialog(
                            requireContext(),
                            object : TripBookedSuccessDialog.DialogCallback {
                                override fun onDialogDismiss() {}
                            }
                        )
                        tripBookedSuccessDialog.showDialog()
                    }

                    override fun onNoSuccess(message: String) {
                        toastUtil.shortToast(message)
                    }

                    override fun onFailure(message: String) {
                        toastUtil.shortToast(message)
                    }

                    override fun onCancel(message: String) {
                        toastUtil.shortToast(message)
                    }
                }
            )
        }
    }

    companion object {
        private const val TAG = "TripDetailsFragment"
    }
}