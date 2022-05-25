package com.example.letsgoadmin.ui.fragment

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letsgoadmin.R
import com.example.letsgoadmin.adapter.TripPicsAdapter
import com.example.letsgoadmin.callback.ImageUriListener
import com.example.letsgoadmin.database.remote.callback.FirebaseCallback
import com.example.letsgoadmin.database.remote.controller.DatabaseController
import com.example.letsgoadmin.databinding.FragmentAddPlaceBinding
import com.example.letsgoadmin.model.Place
import com.example.letsgoadmin.storage.callback.UploadFileCallback
import com.example.letsgoadmin.ui.activity.HomeActivity
import com.example.letsgoadmin.util.constant.Constants
import com.example.letsgoadmin.util.extension.getLoadingDialog
import com.example.letsgoadmin.util.extension.isEmpty
import com.example.letsgoadmin.util.toast.ToastUtil
import com.example.smartretailadmin.storage.controller.StorageController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.listener.DismissListener
import org.koin.android.ext.android.inject
import java.util.*

class AddPlaceFragment : Fragment(), ImageUriListener {

    private lateinit var binding: FragmentAddPlaceBinding
    private val toastUtil: ToastUtil by inject()
    private val databaseController: DatabaseController by inject()
    private val storageController: StorageController by inject()
    private lateinit var tripPicsAdapter: TripPicsAdapter
    private lateinit var loadingDialog: Dialog

    // Image urls of the trip
    private var imgUrls = Constants.NULL_STRING
    private var fbStorageImgUrls = Constants.NULL_STRING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        loadingDialog = requireContext().getLoadingDialog(
            String.format(getString(R.string.upload_image_title)),
            String.format(getString(R.string.upload_image_desc))
        )
        (requireActivity() as HomeActivity).setImageUrlListenerCallback(this)
        setListeners()
    }

    private fun setListeners() {
        binding.btnPost.setOnClickListener {
            postPlace()
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
    }

    companion object {
        private const val TAG = "AddPlaceFragment"
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

        populatePlacesPicsAdapter()
    }

    private fun populatePlacesPicsAdapter() {
        tripPicsAdapter = TripPicsAdapter(requireContext(), imgUrls.split(','))
        binding.rvPics.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvPics.adapter = tripPicsAdapter
    }

    private fun postPlace() {
        val validationResult = validateFields()
        if (validationResult) {
            val placeId = databaseController.getUniquePlaceId()
            placeId?.let {
                with(binding) {

                    val perPersonFair = edtPerPersonFair.text.toString().toInt()

                    databaseController.addPlace(
                        Place(
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
                toastUtil.longToast("Please specify the per person fair to visit this place")
                return false
            }
        }
        return true
    }

}