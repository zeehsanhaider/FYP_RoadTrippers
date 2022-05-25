package com.example.letsgoadmin.util.storage

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object StorageReferences {

    private const val tripImagesStorageRefPath: String = "/Trip_Images"

    lateinit var tripImagesStorageRef: StorageReference

    fun initStorageReferences() {
        tripImagesStorageRef = FirebaseStorage.getInstance().getReference(
            tripImagesStorageRefPath
        )
    }

}