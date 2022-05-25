package com.example.letsgoadmin.storage.callback

import android.net.Uri

interface UploadFileCallback {
    
    fun onUploadComplete(uri: Uri)

    fun onUploadProgress(progress: Int)

    fun onUploadFailure(message: String)

    fun onUploadPaused()

}