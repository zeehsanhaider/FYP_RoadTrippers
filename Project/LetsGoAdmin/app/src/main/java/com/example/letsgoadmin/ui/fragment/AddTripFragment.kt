package com.example.letsgoadmin.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letsgoadmin.R
import com.example.letsgoadmin.adapter.TripPicsAdapter
import com.example.letsgoadmin.callback.ImageUriListener
import com.example.letsgoadmin.database.remote.callback.FirebaseCallback
import com.example.letsgoadmin.database.remote.controller.DatabaseController
import com.example.letsgoadmin.databinding.FragmentAddTripBinding
import com.example.letsgoadmin.model.Trip
import com.example.letsgoadmin.storage.callback.UploadFileCallback
import com.example.letsgoadmin.ui.activity.HomeActivity
import com.example.letsgoadmin.util.constant.Constants
import com.example.letsgoadmin.util.extension.getLoadingDialog
import com.example.letsgoadmin.util.extension.isEmpty
import com.example.letsgoadmin.util.extension.visible
import com.example.letsgoadmin.util.toast.ToastUtil
import com.example.smartretailadmin.storage.controller.StorageController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.listener.DismissListener
import org.koin.android.ext.android.inject
import java.util.*

class AddTripFragment : Fragment(), ImageUriListener {

    private lateinit var binding: FragmentAddTripBinding
    private val toastUtil: ToastUtil by inject()
    private val databaseController: DatabaseController by inject()
    private val storageController: StorageController by inject()
    private lateinit var tripPicsAdapter: TripPicsAdapter
    private lateinit var loadingDialog: Dialog

    // Date and Time saving fields
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mHour = 0
    private var mMinute = 0

    private var departureDate = Constants.NULL_STRING
    private var departureTime = Constants.NULL_STRING

    // Image urls of the trip
    private var imgUrls = Constants.NULL_STRING
    private var fbStorageImgUrls = Constants.NULL_STRING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {

//        val id = databaseController.getUniqueVehicleId()
//        id?.let {
//            val vehicle = DataGenerator.getVehiclesData()[3]
//            vehicle.id = id
//            databaseController.addVehicle(
//                vehicle,
//                object : FirebaseCallback {
//                    override fun onSuccess(message: String) {
//                        Log.e(TAG, "onSuccess: $message")
//                    }
//                    override fun onNoSuccess(message: String) {}
//                    override fun onFailure(message: String) {}
//                    override fun onCancel(message: String) {}
//                }
//            )
//        }


        loadingDialog = requireContext().getLoadingDialog(
            String.format(getString(R.string.upload_image_title)),
            String.format(getString(R.string.upload_image_desc))
        )
        (requireActivity() as HomeActivity).setImageUrlListenerCallback(this)
        setListeners()
    }

    private fun setListeners() {
        binding.btnPost.setOnClickListener {
            postTrip()
        }
        binding.btnAddThingsTodo.setOnClickListener {

        }
        binding.btnAddPhoto.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .setDismissListener(
                    object : DismissListener {
                        override fun onDismiss() {
                            Log.e(TAG, "onDismiss: Image picking from gallery dismissed.")
                        }
                    }
                )
                .createIntent {
                    (requireActivity() as HomeActivity).startForProfileImageResult.launch(it)
                }
        }
        binding.cardDepartureDate.setOnClickListener {
            showDialogDatePicker()
        }
        binding.cardDepartureTime.setOnClickListener {
            showDialogTimePicker()
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
            OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                toastUtil.shortToast("$hourOfDay, $minute")
                departureTime = "$hourOfDay : $minute"
                binding.txDepartureTime.visible()
                binding.txDepartureTime.text = departureTime
            }, mHour, mMinute, true
        )
        timePickerDialog.show()
    }

    private fun postTrip() {
        val validationResult = validateFields()
        if (validationResult) {
            val tripId = databaseController.getUniqueTripId()
            tripId?.let {
                with(binding) {

                    val tripDays = edtDays.text.toString().toInt()
                    val maxSeats = edtSeatsAvailable.text.toString().toInt()
                    val perPersonFair = edtPerPersonFair.text.toString().toInt()

                    databaseController.postTrip(
                        Trip(
                            id = it,
                            title = edtTitle.text.toString(),
                            description = edtDescription.text.toString(),
                            dateCreated = Date().time,
                            expensePerPerson = perPersonFair,
                            imageUrls = fbStorageImgUrls
                        ),
                        object : FirebaseCallback {
                            override fun onSuccess(message: String) {
                                toastUtil.shortToast(message)
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
        }
    }

    private fun validateFields(): Boolean {
        with(binding) {
            if (edtTitle.isEmpty()) {
                toastUtil.longToast("Please specify the title")
                return false
            }
            if (edtDescription.isEmpty()) {
                toastUtil.longToast("Please specify the description")
                return false
            }
            if (edtPerPersonFair.isEmpty()) {
                toastUtil.longToast("Please specify the per person fair of the trip")
                return false
            }
            if (edtSeatsAvailable.isEmpty()) {
                toastUtil.longToast("Please give the maximum amount of persons to go on the trip")
                return false
            }
            if (edtDays.isEmpty()) {
                toastUtil.longToast("Please give number of days the trip will take")
                return false
            }
            if (departureDate == Constants.NULL_STRING) {
                toastUtil.longToast("Please select departure date")
                return false
            }
            if (departureTime == Constants.NULL_STRING) {
                toastUtil.longToast("Please select departure time")
                return false
            }
        }
        return true
    }

    companion object {
        private const val TAG = "AddTripFragment"
    }

    override fun onImageUriReceived(uri: Uri) {
        if (imgUrls != Constants.NULL_STRING) {
            imgUrls += ",$uri"
        } else {
            imgUrls = uri.toString()
        }
        Log.e(TAG, "onImageUriReceived: incoming uri: $imgUrls")

        loadingDialog.show()

        storageController.uploadFromUri(uri, object : UploadFileCallback {
            override fun onUploadComplete(uri: Uri) {
                if (fbStorageImgUrls != Constants.NULL_STRING) {
                    fbStorageImgUrls += ",$uri"
                } else {
                    fbStorageImgUrls = uri.toString()
                }
                Log.e(TAG, "onUploadComplete: firebase storage url: $fbStorageImgUrls")
                loadingDialog.dismiss()
            }

            override fun onUploadProgress(progress: Int) {}
            override fun onUploadFailure(message: String) {}
            override fun onUploadPaused() {}
        })

        populateTripPicsAdapter()
    }

    private fun populateTripPicsAdapter() {
        tripPicsAdapter = TripPicsAdapter(requireContext(), imgUrls.split(','))
        binding.rvPics.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvPics.adapter = tripPicsAdapter
    }

}