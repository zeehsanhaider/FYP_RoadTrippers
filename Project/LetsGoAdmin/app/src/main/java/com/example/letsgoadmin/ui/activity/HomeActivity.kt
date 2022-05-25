package com.example.letsgoadmin.ui.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.letsgoadmin.R
import com.example.letsgoadmin.callback.ImageUriListener
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var btmNavView: BottomNavigationView
    private lateinit var mNavController: NavController
    private lateinit var imageUriListener: ImageUriListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initComponents()
    }

    private fun initComponents() {
        setupNavigationView()
    }

    private fun setupNavigationView() {
        btmNavView = findViewById(R.id.btm_nav_view)
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(btmNavView, mNavController)
    }

    fun setImageUrlListenerCallback(imageUriListener: ImageUriListener) {
        this.imageUriListener = imageUriListener
    }

    val startForProfileImageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data

        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                Log.e(TAG, "FileUri: $fileUri")
                imageUriListener.onImageUriReceived(fileUri)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "HomeActivity"
    }

}