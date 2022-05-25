package com.example.roadtrippers.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.roadtrippers.adapter.VehiclesAdapter
import com.example.roadtrippers.database.local.UserDatabase
import com.example.roadtrippers.database.remote.callback.DataChangeListener
import com.example.roadtrippers.database.remote.callback.FirebaseCallback
import com.example.roadtrippers.database.remote.controller.DatabaseController
import com.example.roadtrippers.databinding.FragmentBookTripBinding
import com.example.roadtrippers.model.CustomTripTicket
import com.example.roadtrippers.model.Vehicle
import com.example.roadtrippers.util.constant.Constants.NULL_STRING
import com.example.roadtrippers.util.extension.isEmpty
import com.example.roadtrippers.util.extension.setLinearLayoutManager
import com.example.roadtrippers.util.extension.visible
import com.example.roadtrippers.util.navigation.NavigationUtils
import com.example.roadtrippers.util.toast.ToastUtil
import org.koin.android.ext.android.inject
import java.util.*

class BookTripFragment : Fragment() {

    private lateinit var binding: FragmentBookTripBinding
    private val databaseController: DatabaseController by inject()
    private val toastUtil: ToastUtil by inject()
    private val userDatabase: UserDatabase by inject()
    private val args: BookTripFragmentArgs by navArgs()
    private lateinit var vehiclesAdapter: VehiclesAdapter

    // Date and Time saving fields
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mHour = 0
    private var mMinute = 0

    private var departureDate = NULL_STRING
    private var departureTime = NULL_STRING

    private var selectedVehicleId: String = NULL_STRING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        setVehiclesRv()
        fetchVehicles()
        setListeners()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListeners() {
        binding.cardDepartureDate.setOnClickListener {
            showDialogDatePicker()
        }
        binding.cardDepartureTime.setOnClickListener {
            showDialogTimePicker()
        }
        binding.btnDone.setOnClickListener {
            prepareTripTicket()
        }
        vehiclesAdapter.setOnItemClickListener { vehicle, position ->
            selectedVehicleId = vehicle.id
            vehiclesAdapter.notifyItemChanged(vehiclesAdapter.selectedPosition)
            vehiclesAdapter.selectedPosition = position
            vehiclesAdapter.notifyItemChanged(vehiclesAdapter.selectedPosition)
        }
    }

    private fun showDialogDatePicker() {
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        @SuppressLint("SetTextI18n") val datePickerDialog = DatePickerDialog(
            requireContext(), { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                departureDate = "$dayOfMonth ${monthOfYear + 1}, $year"
                binding.txDepartureDate.visible()
                binding.txDepartureDate.text = departureDate
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    private fun showDialogTimePicker() {
        val c = Calendar.getInstance()
        mHour = c[Calendar.HOUR_OF_DAY]
        mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog
        @SuppressLint("SetTextI18n") val timePickerDialog = TimePickerDialog(
            context,
            { _: TimePicker?, hourOfDay: Int, minute: Int ->
                toastUtil.shortToast("$hourOfDay, $minute")
                departureTime = "$hourOfDay : $minute"
                binding.txDepartureTime.visible()
                binding.txDepartureTime.text = departureTime
            }, mHour, mMinute, true
        )
        timePickerDialog.show()
    }

    private fun validateFields(): Boolean {
        with(binding) {
            if (edtContact.isEmpty()) {
                toastUtil.longToast("Please specify your contact number")
                return false
            }
            if (edtPersons.isEmpty()) {
                toastUtil.longToast("Please specify how many persons are going")
                return false
            }
            if (edtDays.isEmpty()) {
                toastUtil.longToast("Please specify days for the trip")
                return false
            }
            if (txDepartureDate.isEmpty()) {
                toastUtil.longToast("Please specify departure date for the trip")
                return false
            }
            if (txDepartureTime.isEmpty()) {
                toastUtil.longToast("Please specify departure time for the trip")
                return false
            }
            if (selectedVehicleId == NULL_STRING) {
                toastUtil.longToast("Please select a transport for your trip")
                return false
            }
        }
        return true
    }

    private fun prepareTripTicket() {
        val validations = validateFields()
        if (validations) {
            val ticketId = databaseController.getUniqueCustomTripTicketId()
            ticketId?.let {

                val contact = binding.edtContact.text.toString()
                val persons = binding.edtPersons.text.toString().toInt()
                val days = binding.edtDays.text.toString().toInt()
                val totalExpense = ((args.selectedPlace.expensePerPerson * persons).toDouble() * vehiclesAdapter.getSelectedVehicle().expenseFactor).toInt()

                databaseController.addCustomTripTicket(
                    CustomTripTicket(
                        id = it,
                        userId = userDatabase.getCurrUser().id,
                        placeId = args.selectedPlace.id,
                        vehicleId = selectedVehicleId,
                        contact = contact,
                        persons = persons,
                        departureTime = binding.txDepartureTime.text.toString(),
                        departureDate = binding.txDepartureDate.text.toString(),
                        days = days,
                        dateCreated = Date().time,
                        totalExpense = totalExpense
                    ),
                    object : FirebaseCallback {
                        override fun onSuccess(message: String) {
                            toastUtil.shortToast(message)
                            NavigationUtils.navigateBack(binding.root)
                        }

                        override fun onNoSuccess(message: String) {
                            toastUtil.shortToast(message)
                            NavigationUtils.navigateBack(binding.root)
                        }

                        override fun onFailure(message: String) {
                            toastUtil.shortToast(message)
                            NavigationUtils.navigateBack(binding.root)
                        }

                        override fun onCancel(message: String) {
                            toastUtil.shortToast(message)
                            NavigationUtils.navigateBack(binding.root)
                        }
                    }
                )
            }
        }
    }

    private fun setVehiclesRv() {
        setLinearLayoutManager(
            requireContext(),
            binding.rvVehicles,
            true
        )
        vehiclesAdapter = VehiclesAdapter(requireContext())
        binding.rvVehicles.adapter = vehiclesAdapter
    }

    private fun fetchVehicles() {
        databaseController.getAllVehicles(
            object : DataChangeListener {
                override fun onDataChange(list: List<Any>) {
                    val incomingVehicles: List<Vehicle> = list.filterIsInstance<Vehicle>()
                    vehiclesAdapter.differ.submitList(incomingVehicles)
                    binding.txSelectVehicle.visible()

                    Log.e(TAG, "onDataChange: $incomingVehicles")
                }

                override fun onCancel(message: String) {
                    toastUtil.shortToast(message)
                }
            }
        )
    }

    companion object {
        private const val TAG = "BookTripFragment"
    }

}