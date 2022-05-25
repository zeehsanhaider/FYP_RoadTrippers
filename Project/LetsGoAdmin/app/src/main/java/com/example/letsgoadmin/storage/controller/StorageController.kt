package com.example.smartretailadmin.storage.controller

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.letsgoadmin.util.storage.StorageReferences
import com.example.letsgoadmin.storage.callback.UploadFileCallback
import com.example.smartretailadmin.util.storage.PathUtil
import com.google.firebase.storage.UploadTask
import java.io.File
import java.net.URISyntaxException

class StorageController(private val context: Context) {

    companion object {
        private const val TAG = "StorageController"
    }

    private val productImagesRef = StorageReferences.tripImagesStorageRef
    private lateinit var fileUploadTask: UploadTask

    fun uploadFromUri(fileUri: Uri, callback: UploadFileCallback) {
        // Works below OREO
        try {
            val filePath = PathUtil.getPath(context, fileUri)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        // WORKS ABOVE OREO
        //create path from uri
        val file = File(fileUri.path)
        //split the path.
        val split = file.path.split(":").toTypedArray()
        //assign it to a string(your choice).
        val filePath = split[0]
        Log.e(TAG, "uploadFromUri: $filePath")

        val currFile = Uri.fromFile(File(filePath))
        val currFileStorageRef = productImagesRef.child(currFile.lastPathSegment!!)

        fileUploadTask = currFileStorageRef.putFile(currFile)

        fileUploadTask.addOnFailureListener { exception ->
            exception.let {
                callback.onUploadFailure(it.localizedMessage)
            }
        }.addOnSuccessListener {
            currFileStorageRef.downloadUrl.addOnSuccessListener { uri ->
                callback.onUploadComplete(uri)
            }
        }.addOnProgressListener { taskSnapshot ->
            val progress =
                (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
            callback.onUploadProgress(progress)
        }.addOnPausedListener {
            callback.onUploadPaused()
        }
    }

}